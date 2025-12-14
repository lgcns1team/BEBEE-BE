-- 1. member1_id에 대한 외래 키 추가
ALTER TABLE chatroom
    ADD CONSTRAINT fk_chatroom_member1
        FOREIGN KEY (member1_id) REFERENCES member_sync (member_id);

-- 2. member2_id에 대한 외래 키 추가
ALTER TABLE chatroom
    ADD CONSTRAINT fk_chatroom_member2
        FOREIGN KEY (member2_id) REFERENCES member_sync (member_id);