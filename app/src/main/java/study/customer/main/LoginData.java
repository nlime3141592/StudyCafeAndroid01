package study.customer.main;

public class LoginData
{
    private int m_uuid;
    private String m_id;
    private String m_nickname;

    public void setUuid(int _uuid) { m_uuid = _uuid; }
    public int getUuid() { return m_uuid; }

    public void setId(String _id) { m_id = _id; }
    public String getId() { return m_id; }

    public void setNickname(String _nickname) { m_nickname = _nickname; }
    public String getNickname() { return m_nickname; }
}
