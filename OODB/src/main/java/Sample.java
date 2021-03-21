import com.db4o.Db4o;
import com.db4o.ObjectContainer;

import java.util.List;

public class Sample {
    public static void main(String[] args) {
        Employee emp = new Employee(1, "abc");
        ObjectContainer db = Db4o.openFile("Demo");

        try{
            db.store(emp);
            List<Employee> l = db.query(Employee.class);
            for (Employee emp1 : l){
                System.out.println(emp1);
            }
        }
        finally {
            db.close();
        }

    }
}
