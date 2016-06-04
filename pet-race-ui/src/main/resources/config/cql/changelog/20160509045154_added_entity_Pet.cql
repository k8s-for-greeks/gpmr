CREATE TABLE pet (
    "petId" uuid PRIMARY KEY,
    description text,
    name text,
    "petCategoryId" uuid,
    "petCategoryName" text,
    "petSpeed" float
);

CREATE INDEX pet_petCategoryId_idx ON pet ("petCategoryId");
CREATE INDEX pet_name_idx ON pet (name);
CREATE INDEX pet_petCategoryName_idx ON pet ("petCategoryName");

