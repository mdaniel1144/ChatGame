package com.example.gamechats_final.Object;

public class Enums {
    public enum MenuOption {
        UserMember,
        ChatForYou,
        ChatGroup
    }

    public enum RecyclerViewType {

        Friend,
        CategoryTags,
        PlatformGameTags,
        UserMember,
        Chat
    }

    public enum MessageType {

        Sender(-1),
        Receive(1),
        Date(0);
        public final int type;
        private MessageType(int type) {
            this.type = type;
        }
    }
}
