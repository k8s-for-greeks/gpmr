
CREATE TABLE IF NOT EXISTS pet (
    id uuid,
    name text,
    petCategory text,
    petCategoryId uuid,
    petSpeed decimal,
    PRIMARY KEY(id)
);
