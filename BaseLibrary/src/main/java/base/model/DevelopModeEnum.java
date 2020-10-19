package base.model;

public class DevelopModeEnum {
    public enum DevelopMode{
        切换认证中心URL(1);

        public int mode;
        DevelopMode(int mode) {
            this.mode = mode;
        }
    }

    public enum AuthUrlMode{
        market(0),
        测试(1),
        测试预发布(2),
        生产(3),
        生产预发布(4),
        自定义(5),
        开发(6);

        public int mode;

        AuthUrlMode(int mode) {
            this.mode = mode;
        }
    }
}
