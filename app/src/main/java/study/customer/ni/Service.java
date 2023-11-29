package study.customer.ni;

public abstract class Service implements IService
{
    private volatile boolean m_serviceStarted;
    private volatile boolean m_serviceEnded;
    private volatile boolean m_serviceExecutionResult;

    public Service()
    {
        m_serviceStarted = false;
        m_serviceEnded = false;
        m_serviceExecutionResult = false;
    }

    public abstract boolean tryExecuteService();

    public final boolean isServiceStarted()
    {
        return m_serviceStarted;
    }

    public final boolean isServiceEnded()
    {
        return m_serviceStarted && m_serviceEnded;
    }

    public final boolean isExecutionSuccess()
    {
        return m_serviceExecutionResult;
    }

    public final void onServiceStart()
    {
        m_serviceStarted = true;
    }

    public final void onServiceEnd(boolean _serviceExecutionResult)
    {
        m_serviceExecutionResult = _serviceExecutionResult;
        m_serviceEnded = true;
    }
}