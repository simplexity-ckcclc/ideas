CREATE TABLE test (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(32) NOT NULL,
  age int(11) NOT NULL,
  city varchar(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name_age_idx (name, age)
);