package com.zncm.mxtg.data;

public class EnumData {

    public enum RefreshEnum {

        PROJECT(1, "project");
        private int value;
        private String strName;

        private RefreshEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum StatusEnum {

        ON(1, "ON"), OFF(2, "OFF"), FINISH(3, "FINISH"), DEL(4, "DEL");
        private int value;
        private String strName;

        private StatusEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }


    public enum SortEnum {

        TIME_DOWN(1, "创建时间 ↓"), TIME_UP(2, "创建时间 ↑"), SPEND_DOWN(3, "耗时 ↓"), SPEND_UP(4, "耗时 ↑");
        private int value;
        private String strName;

        private SortEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }
        public static SortEnum valueOf(int value) {
            for (SortEnum typeEnum : SortEnum.values()) {
                if (typeEnum.getValue() == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }


    public enum FrequencyEnum {

        ONCE(1, "一次"), NORMAL(2, "日常");
        private int value;
        private String strName;

        private FrequencyEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


        public static FrequencyEnum valueOf(int value) {
            for (FrequencyEnum typeEnum : FrequencyEnum.values()) {
                if (typeEnum.getValue() == value) {
                    return typeEnum;
                }
            }
            return null;
        }


    }


}
