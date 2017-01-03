package online.senya.termigram.terminal;

/**
 * Created by Semyon on 03.01.2017.
 */
public class UserCredentials {

    private final String login;

    private final String password;

    public UserCredentials(final String login, final String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
