
CREATE TABLE IF NOT EXISTS raceResults (
    id uuid,
    petId uuid,
    petName text,
    petCategory text,
    petCategoryId uuid,
    place int,
    time timestamp,
    PRIMARY KEY(id)
);
