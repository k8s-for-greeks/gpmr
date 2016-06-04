
CREATE TABLE pet_category (
    "petCategoryId" uuid PRIMARY KEY,
    name text,
    speed float
);

CREATE INDEX pet_category_name_idx ON pet_category (name);
