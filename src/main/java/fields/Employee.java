package fields;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class Employee {
    public static final int SALARY_RANGE = 400;
    public static final int MIN_SALARY = 200;

    private final Random random = new Random();

    protected String firstName;
    protected String lastName;
    private int salary;

    protected Employee() {
        this.salary = MIN_SALARY + random.nextInt(SALARY_RANGE);
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public int getSalary() { return salary; }

    /**
     * Employee Builder class
     */
    public static class Builder {
        protected String builderFirstName;
        protected String builderLastName;

        public Builder setFirstName(String firstName) {
            this.builderFirstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.builderLastName = lastName;
            return this;
        }

        public Employee build() {return new EmployeeImpl(); }

        /**
         * Concrete Employee implementation
         */
        private class EmployeeImpl extends Employee {
            EmployeeImpl() {
                this.firstName = builderFirstName;
                this.lastName = builderLastName;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.stream(Employee.class.getFields()).collect(Collectors.toList()));
    }
}