-- 1. member1_id에 대한 외래 키 추가
ALTER TABLE chatroom
    ADD CONSTRAINT fk_chatroom_member1
        FOREIGN KEY (member1_id) REFERENCES member_sync (member_id);

-- 2. member2_id에 대한 외래 키 추가
ALTER TABLE chatroom
    ADD CONSTRAINT fk_chatroom_member2
        FOREIGN KEY (member2_id) REFERENCES member_sync (member_id);

ALTER TABLE chatroom_help_category
    ADD CONSTRAINT FK_chatroom_help_category_TO_chat_help_category_sync
        FOREIGN KEY (help_category_Id) REFERENCES `help_category_sync` (`help_category_id`)
