db.createCollection("chat", {
    validator:{
        $jsonSchema: {
            bsonType: "object",
            required: ["_id", "chatroom_id", "type", "created_at"],
            properties: {
                "_id": {
                    bsonType: "long"
                },
                "chatroom_id": {
                    bsonType: "long"
                },
                "sender_id": {
                    bsonType: "long"
                },
                "type": {
                    bsonType: "string",
                    enum: ["TEXT", "IMAGE", "MATCH_SUCCESS", "MATCH_FAILURE", "MATCH_CONFIRMATION"]
                },
                "text_content": {
                    bsonType: "string"
                },
                "attachments": {
                    bsonType: "array",
                    items: {
                        bsonType: "string"
                    }
                },
                "match_confirmation_content": {
                    bsonType: "object",
                    required: ["type", "start_date", "schedule", "location", "points", "help_category", "status"],
                    properties: {
                        "agreementId": {
                            bsonType: "long"
                        },
                        "type": {
                            bsonType: "string"
                        },
                        "start_date": {
                            bsonType: "string",
                            description: "YYYY.MM.DD"
                        },
                        "end_date": {
                            bsonType: "string",
                            description: "YYYY.MM.DD"
                        },
                        "schedule": {
                            bsonType: "array",
                            items: {
                                bsonType: "object",
                                required: ["day", "start_time", "end_time"],
                                properties: {
                                    "day": {
                                        bsonType: "string",
                                        enum: ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"]
                                    },
                                    "start_time": {
                                        bsonType: "string",
                                        description: "HH:mm"
                                    },
                                    "end_time": {
                                        bsonType: "string",
                                        description: "HH:mm"
                                    }
                                }
                            }
                        },
                        "location": {
                            bsonType: "string"
                        },
                        "points": {
                            bsonType: "object",
                            required: ["unit_points", "total"],
                            properties: {
                                "unit_points": {
                                    bsonType: "int"
                                },
                                "total": {
                                    bsonType: "int"
                                }
                            }
                        },
                        "help_category": {
                            bsonType: "array",
                            items: {
                                bsonType: "string"
                            }
                        },
                        "status": {
                            bsonType: "string",
                            enum: ["PENDING", "REJECTED", "ACCEPTED"]
                        }
                    }
                },
                "created_at": {
                    bsonType: "date"
                }
            }
        }
    }
})

db.createCollection("last_read_chat", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["_id", "chat_room_id", "reader_id", "last_read_chat_id", "created_at", "updated_at"],
            properties: {
                "_id": {
                    bsonType: "long"
                },
                "chat_room_id": {
                    bsonType: "long"
                },
                "reader_id": {
                    bsonType: "long"
                },
                "last_read_chat_id": {
                    bsonType: "long"
                },
                "created_at": {
                    bsonType: "date"
                },
                "updated_at": {
                    bsonType: "date"
                }
            }
        }
    }
});