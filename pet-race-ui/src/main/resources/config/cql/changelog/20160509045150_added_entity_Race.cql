
CREATE TABLE IF NOT EXISTS race (
    id uuid,
    numOfPets int,
    length int,
    numOfSamples int,
    winnerId uuid,
    winnerName text,
    winnnerPetCategory text,
    startTime timestamp,
    endTime timestamp,
    PRIMARY KEY(id)
);
