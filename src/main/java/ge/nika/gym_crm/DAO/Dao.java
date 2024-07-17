package ge.nika.gym_crm.DAO;

public interface Dao<T, Id> {
    void create(T t);

    T select(Id id);
}
