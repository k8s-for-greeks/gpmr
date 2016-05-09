
CREATE TABLE IF NOT EXISTS pets (
    id uuid,
    name text,
    petCategory text,
    petCategoryId uuid,
    petSpeed decimal,
    PRIMARY KEY(id)
);
