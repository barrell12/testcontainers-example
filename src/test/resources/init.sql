create table pet(
   id VARCHAR(100) NOT NULL,
   name VARCHAR(100) NOT NULL,
   age INT NOT NULL,
   PRIMARY KEY ( id )
);

INSERT INTO pet (id, name, age) VALUES ('init_pet', 'InitScriptPet', 10);