
CREATE TABLE IF NOT EXISTS raceParticipant (
    id uuid,
    petId uuid,
    petName text,
    petType text,
    petColor text,
    petCategory text,
    petCategoryId text,
    raceId uuid,
    PRIMARY KEY(id)
);
