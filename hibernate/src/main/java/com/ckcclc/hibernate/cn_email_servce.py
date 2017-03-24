#coding=utf8
'''
@author: heruilong
'''
from ConfigParser import SafeConfigParser
import datetime
from datetime import timedelta
import logging
import logging.config
import sys
import os
import glob
from email._parseaddr import COMMASPACE
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.header import Header
import smtplib

PRO_DIR_PATH = os.path.dirname(os.path.realpath(__file__))
LIB_DIR_PATH = os.path.join(PRO_DIR_PATH, 'lib')
for lib_path in glob.glob(os.path.join(LIB_DIR_PATH, '*.egg')):
            sys.path.insert(0, lib_path)    

try:
    from cassandra.cluster import Cluster
    from cassandra.query import BatchStatement
    from cassandra import ConsistencyLevel
except ImportError, e:
    sys.exit('Failed to import Cassandra driver libraries of Python.')
    
CONF_LOG = os.path.join(PRO_DIR_PATH,"logging.properties")
LOG_PATH = os.path.join(PRO_DIR_PATH,"log")
rotate_handler_arg = '("' + (os.path.join(LOG_PATH, "device_validate_error_email.log")) + '","a",20*1024*1024, 20)'
log_default = {'args': rotate_handler_arg}  
logging.config.fileConfig(CONF_LOG, defaults=log_default) 
logger = logging.getLogger()


def send_raw_email_1(smtp_host, smtp_port, sender, recipients, message, error_count, cc):
    smtp_server = smtplib.SMTP(smtp_host, int(smtp_port))

    now = datetime.datetime.now().strftime("%Y-%m-%d")
    
    subject = u"【云运维】内销设备验证异常 ," + u" 时间：" + now + "\n"
    title_fstr = "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n"
    title = title_fstr %(u"设备id",u"地区",u"异常类型",u"设备mac",u"云端mac",u"设备硬件id",u"云端硬件id",u"固件id",u"机型",u"ip地址",u"接入时间",u"首次接入时间")
    message = subject + "\n" + title + message
    
    charset = "UTF-8"
    sub_h = Header(subject, charset)
    outter = MIMEMultipart()
    outter['Subject'] = sub_h
    outter['To'] = COMMASPACE.join(recipients)
    outter['From'] = sender
    outter['Cc'] = COMMASPACE.join(cc)
    
    #attachment
    subtype = "octet-stream"
    attach_file = MIMEText(message, _subtype=subtype, _charset=charset)
    attach_file.add_header('Content-Disposition', 'attachment', filename="device_validate_error-"+  now + ".txt")
    outter.attach(attach_file)
    
    #email text message
    inline_message = u"共有" + str(error_count) + u"台设备接入异常，详细信息请参看附件"
    text = MIMEText(inline_message, _charset=charset)
    outter.attach(text)
    
    composed = outter.as_string()
    #print composed.decode("utf-8")
    smtp_server.sendmail(sender, recipients+cc, composed)
    logger.info("send email")
    smtp_server.quit()

def send_no_new_error_msg(smtp_host, smtp_port, sender, recipients,cc):
    smtp_server = smtplib.SMTP(smtp_host, int(smtp_port))
    now = datetime.datetime.now().strftime("%Y-%m-%d")
    subject = u"【云运维】内销设备验证异常 ," + u" 时间：" + now + "\n"
    
    charset = "UTF-8"
    sub_h = Header(subject, charset)
    outter = MIMEMultipart()
    outter['Subject'] = sub_h
    outter['To'] = COMMASPACE.join(recipients)
    outter['From'] = sender
    outter['Cc'] = COMMASPACE.join(cc)
    #email text message
    inline_message = u"没有设备接入异常"
    text = MIMEText(inline_message, _charset=charset)
    outter.attach(text)
    composed = outter.as_string()
    smtp_server.sendmail(sender, recipients+cc, composed)
    logger.info("send no new validation error email")
    smtp_server.quit()
    
def get_messages(server_ip, db):
    # connect to db
    try:
        cluster = Cluster(server_ip, protocol_version=3)
        session = cluster.connect(db)
    except Exception:
        logger.exception("connect to cassandra failed")
        sys.exit()
    
    common_cql = '''select device_id, alias, cloud_hw_id, cloud_mac, device_hw_id, 
        device_mac, validate_timestamp, region, device_model, hw_ver, device_name, error_code, fw_id, fw_ver, 
        ip_address, tcsp_ver, first_validate_timestamp
        from device_validate_error_info where is_sent = false and error_code = '%s' allow filtering'''
    
    # fetch all records which has not sent email
    id_not_found_cql = common_cql % ("Device id not found")
    info_not_match_cql = common_cql % ("Device info is not match")
    
    # after send email, update is_sent to true
    update_cql = session.prepare('''update device_validate_error_info 
             set email_sent_time = ?, is_sent = true where device_id = ? and device_mac = ? and device_hw_id = ?''')
    
    update_info_list = []
    
    # python cassandra driver treat the time as utc time, 
    # so have to minus 8 hours
    now = datetime.datetime.now() - timedelta(hours=8)
    # set email content header
    message = ""
    
    #device_model
    one_dev_str = ("%s,%s,%s,%s,%s,%s,%s,%s,%s %s,%s,%s,%s")
    
    #count how many abnormal device
    error_count = 0
    
    #device id not found
    rows = []
    try:
        rows = session.execute(id_not_found_cql)
    except Exception:
        logger.exception("execute id_not_found_cql fail")
        
    for (device_id, alias, cloud_hw_id, cloud_mac, device_hw_id, device_mac,
         validate_timestamp, region, device_model, hw_ver, device_name, error_code, fw_id, fw_ver, ip_address,
            tcsp_ver, first_validate_timestamp) in rows:
            
        error_count += 1
        # one device error info
        #print error_code
        one_dev = one_dev_str % (device_id,region,error_code,device_mac,cloud_mac,device_hw_id,cloud_hw_id,fw_id,
                                 device_model, hw_ver, ip_address,validate_timestamp, first_validate_timestamp)
        
        message += one_dev + "\n"
        #huge batch make update timeout
        #batch.add(update_cql, (now, device_id, device_mac, device_hw_id))
        update_info_list.append((now, device_id, device_mac, device_hw_id))
        logger.info("error_info->" + one_dev)
    
    #device info not match
    rows2 = []
    try:
        rows2 = session.execute(info_not_match_cql)
    except Exception:
        logger.exception("execute info_not_match_cql failed")
        
    for (device_id, alias, cloud_hw_id, cloud_mac, device_hw_id, device_mac,
         validate_timestamp, region, device_model, hw_ver, device_name, error_code, fw_id, fw_ver, ip_address,
            tcsp_ver, first_validate_timestamp) in rows2:
        error_count += 1
        # one device error info
        
        one_dev = one_dev_str % (device_id,region,error_code,device_mac,cloud_mac,device_hw_id,cloud_hw_id,fw_id,
                                 device_model, hw_ver, ip_address,validate_timestamp, first_validate_timestamp)
        message += one_dev + "\n"
        #huge batch make update timeout
        #batch.add(update_cql, (now, device_id, device_mac, device_hw_id))
        update_info_list.append((now, device_id, device_mac, device_hw_id))
        logger.info("error_info->" + one_dev)

    # update db
    batch_update_limit = 300
    batches = []
    idx = 1
    batch_stat = BatchStatement(consistency_level=ConsistencyLevel.ONE)
    for update_info in update_info_list:
        if (idx == batch_update_limit):
            batch_stat.add(update_cql, update_info)
            batches.append(batch_stat)
            logger.info("batch size:%s" % idx)
            #new batch_stat
            batch_stat = BatchStatement(consistency_level=ConsistencyLevel.ONE)
            #count from begin
            idx = 1
        else:
            batch_stat.add(update_cql, update_info)
            #count add one
            idx += 1
    #at last need to add the last piece of batch_stat
    if(idx != 1):
        batches.append(batch_stat)
        logger.info("batch size:%s" % (idx-1))
    logger.info("totally %s batch statement" % len(batches))
    
    for batch in batches:    
        try:
            session.execute(batch)
        except Exception:
            logger.exception("batch execute update_cql failed.[batch=%s]" % batch)
    try:
        session.shutdown()
        cluster.shutdown()
    except:
        logger.exception("shutdown cassandra failed")
    return (error_count,message)


if __name__ == '__main__':
    defaults = {'ses.region':'us-east-1',
            'cassandra.server.ip':'127.0.0.1',
            'cassandra.db.name' :'cloud',
            'recipients':'heruilong@tp-link.com.cn',
            'sender':'noreply@tp-link.com.cn',
            'smtp_host':'internal-prod-elb-mail-internal-1882841969.cn-north-1.elb.amazonaws.com.cn',
            'smtp_port':'25'}
    cf = SafeConfigParser(defaults)
    cf.read(os.path.join(PRO_DIR_PATH,'sys.properties.cn'))
    
    cass_server_ip = cf.get("cassandra", "cassandra.server.ip")
    db = cf.get("cassandra", "cassandra.db.name")
    recipients_str = cf.get("email", "recipients")
    recipients = recipients_str.split(",")
    sender = cf.get("email", "sender")
    smtp_host = cf.get("smtp", "smtp.host")
    smtp_port = cf.get("smtp", "smtp.port")
    
    try:
        cc_str = cf.get("email", "cc")
        cc = cc_str.split(",")
    except Exception as e:
        logger.exception("get configration failed", e)
        sys.exit()
    
    error_count, message = get_messages(cass_server_ip.split(','), db)
    if (error_count != 0):
        send_raw_email_1(smtp_host, smtp_port, sender, recipients, message, error_count, cc)
    else:
        send_no_new_error_msg(smtp_host, smtp_port, sender, recipients,cc)
        
    