package application;
public class AdminAccount extends Account {
    double personalRevenue;

    public AdminAccount(String username, String password) {
        super(username, password);
        personalRevenue = 0;
    }

    public double getPersonalRevenue() {
        return personalRevenue;
    }

    public void setPersonalRevenue(double personalRevenue) {
        this.personalRevenue = personalRevenue;
    }
}
