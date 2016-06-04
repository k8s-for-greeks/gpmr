CREATE TABLE race_participant (
    "raceParticipantId" uuid PRIMARY KEY,
    "petId" uuid,
    "finishPosition" int,
    "finishTime" decimal,
    finished boolean,
    "petCategoryId" uuid,
    "petCategoryName" text,
    "petColor" text,
    "petName" text,
    "raceId" uuid,
    "startTime" timestamp,
);
CREATE INDEX race_participant_petName_idx ON race_participant ("petName");
CREATE INDEX race_participant_raceId_idx ON race_participant ("raceId");
CREATE INDEX race_participant_petColor_idx ON race_participant ("petColor");
CREATE INDEX race_participant_petCategoryId_idx ON race_participant ("petCategoryId");
CREATE INDEX race_participant_finished_idx ON race_participant (finished);
