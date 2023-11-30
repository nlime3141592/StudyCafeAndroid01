package study.customer.main;

public interface IResponsable<T>
{
    void onResponse(T _eventData);
}