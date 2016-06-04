
CREATE TABLE race_normal (
    "raceNormalId" uuid PRIMARY KEY,
    "raceId" uuid,
    "petCategoryId" uuid,
    "currentTime" timestamp,
    "normalLoc" float,
    "normalScale" float,
    "normalSize" int,
    normals list<decimal>,
    "petCategoryName" text
);
