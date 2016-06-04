CREATE TABLE race_result (
    "raceResultId" uuid PRIMARY KEY,
    "raceId" uuid,
    "raceParticipantId" uuid,
    "petCategoryId" uuid,
    "finishPosition" int,
    "finishTime" decimal,
    "petCategoryName" text,
    "petColor" text,
    "petName" text,
    "petType" text,
    "startTime" timestamp,
);
CREATE INDEX race_result_petColorId_idx ON race_result ("petColor");
CREATE INDEX race_result_petName_idx ON race_result ("petName");
