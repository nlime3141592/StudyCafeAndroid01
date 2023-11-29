package study.customer.ni;

public abstract class StudyThread implements Runnable
{
	private boolean m_started;
	private Thread m_thread;

	public StudyThread()
	{
		m_started = false;
		m_thread = new Thread(this);
	}

	@Override
	public abstract void run();

	public void start()
	{
		if(this.getThreadState() != ThreadState.READY)
			return;

		m_started = true;
		m_thread.start();
	}

	public void stop()
	{
		if(this.getThreadState() != ThreadState.RUNNING)
			return;

		m_thread = null;
	}

	public final boolean isRun()
	{
		return this.getThreadState() == ThreadState.RUNNING;
	}

	public final ThreadState getThreadState()
	{
		if(!m_started)
			return ThreadState.READY;
		else if(m_thread != null)
			return ThreadState.RUNNING;
		else
			return ThreadState.END;
	}
}
