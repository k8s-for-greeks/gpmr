
CREATE TABLE race (
    "raceId" uuid PRIMARY KEY,
    "baseSpeed" float,
    description text,
    length int,
    "numOfPets" int,
    "petCategoryId" uuid,
    "petCategoryName" text,
    "racersIds" list<uuid>,
    "startTime" timestamp,
    "winnerId" uuid
);

CREATE INDEX race_numOfPets_idx ON race ("numOfPets");
CREATE INDEX race_petCategoryId_idx ON race ("petCategoryId");
CREATE INDEX race_winnerId_idx ON race ("winnerId");
