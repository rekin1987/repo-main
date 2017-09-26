public class MyJavaClass {

    private int counter;
    private String name;

    public MyJavaClass(int counter, String name) {
        this.counter = counter;
        this.name = name;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

class GitUser {

    private String username;
    private String pass;
    private int colorMode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        this.colorMode = colorMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GitUser gitUser = (GitUser) o;

        if (colorMode != gitUser.colorMode) return false;
        if (!username.equals(gitUser.username)) return false;
        return pass.equals(gitUser.pass);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + pass.hashCode();
        result = 31 * result + colorMode;
        return result;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}