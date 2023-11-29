package study.customer.main;

public class CustomerManager
{
    private static CustomerManager s_m_manager;

    private LoginData m_loginData;

    public static CustomerManager getManager()
    {
        // NOTE:
        // 객체를 호출할 때, 만약 존재하지 않는다면 새로운 객체를 생성해서 할당합니다.
        // 존재한다면 새로운 객체를 생성하는 코드를 무시하고 바로 반환합니다.
        // 이러한 방법으로 CustomerManager 객체가 프로그램 전체에서 딱 1개만 존재함이 보장됩니다.
        if(s_m_manager == null)
            s_m_manager = new CustomerManager();

        return s_m_manager;
    }

    // NOTE: 외부에서 new CustomerManager()를 호출할 수 없도록 생성자를 private으로 선언합니다.
    private CustomerManager()
    {
        m_loginData = new LoginData();
    }

    public LoginData getLoginData() { return m_loginData; }
}