
CREATE TABLE race_data (
    "raceDataId" uuid PRIMARY KEY,
    "petId" uuid,
    finished boolean,
    interval int,
    "petCategoryId" uuid,
    "petCategoryName" text,
    "petName" text,
    "raceId" uuid,
    "runnerDistance" decimal,
    "runnerPosition" int,
    "runnerPreviousDistance" decimal,
    "startTime" timestamp,
);

CREATE INDEX race_data_petCategoryId_idx ON race_data ("petCategoryId");
CREATE INDEX race_data_petName_idx ON race_data ("petName");
CREATE INDEX race_data_raceId_idx ON race_data ("raceId");
