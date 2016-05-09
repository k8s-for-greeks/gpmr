
CREATE TABLE IF NOT EXISTS raceData (
    id uuid,
    petId uuid,
    petName text,
    petCategory text,
    petCategoryId uuid,
    runnerPostion int,
    runnerSashColor text,
    PRIMARY KEY(id)
);
