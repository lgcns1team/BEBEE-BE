db = db.getSiblingDB('bebee');

db.createUser({
    user: "bebee",
    pwd: "bebee1234",
    roles: [
        {
            role: "readWrite",
            db: "bebee"
        }
    ]
})