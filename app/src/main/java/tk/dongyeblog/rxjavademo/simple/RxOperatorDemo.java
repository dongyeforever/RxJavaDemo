package tk.dongyeblog.rxjavademo.simple;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.base.BaseActivity;
import tk.dongyeblog.rxjavademo.bean.Student;
import tk.dongyeblog.rxjavademo.util.LogUtil;

/**
 * description:
 * author： dongyeforever@gmail.com
 * date: 2016-01-21 20:06
 */
public class RxOperatorDemo extends BaseActivity {
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv)
    ImageView iv;

    @Override
    protected int getLayoutId() {
        return R.layout.rx_simple_demo;
    }

    @Override
    public void initView() {
        rxMap();
        rxFlatMap();
        rxScan();
    }

    /**
     * map 对Observable发射的每一项数据应用一个函数，执行变换操作
     */
    private void rxMap() {
        int[] arr = {1, 2, 5, 8, 9};
        Observable.just(arr).map(new Func1<int[], int[]>() {
            @Override
            public int[] call(int[] ints) {
                int[] result = new int[ints.length];
                for (int i = 0; i < ints.length; i++) {
                    result[i] = ints[i] + 10;
                }
                return result;
            }
        }).subscribe(new Action1<int[]>() {
            @Override
            public void call(int[] ints) {
                String s = "";
                for (int i : ints) {
                    s = s + i + ",";
                }
                tv.setText(s);
            }
        });
    }

    /**
     * FlatMap 将一个发射数据的Observable变换为多个Observables,
     * 然后将它们发射的数据合并后放进一个单独的Observable
     */
    private void rxFlatMap() {
        List<Student.Course> courses = new ArrayList<>();
        Student s1 = new Student("悟空", courses);
        Student.Course course1 = s1.new Course("金刚经");
        Student.Course course2 = s1.new Course("般若经");
        courses.add(course1);
        courses.add(course2);
        Student s2 = new Student("八戒", courses);
        Student s3 = new Student("沙僧", courses);

        Student[] students = {s1, s2, s3};
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
                LogUtil.i(s);
                tvName.setText(s);
            }
        };

        //1.打印学生的名字
        Observable.from(students)
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(subscriber);

        //2.打印学生课程 (每个学生只有一个名字，但却有多个课程。)
        Subscriber<Student> studentSubscriber = new Subscriber<Student>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Student student) {
                List<Student.Course> courses = student.getCourses();
                for (int i = 0; i < courses.size(); i++) {
                    Student.Course course = courses.get(i);
                    LogUtil.i(course.getName());
                }
            }
        };
        Observable.from(students)
                .subscribe(studentSubscriber);

        /**
         * 那么如果我不想在 Subscriber 中使用 for 循环，而是希望 Subscriber 中直接传入单个的 Course 对象呢（这对于代码复用很重要）？
         */
        Subscriber<Student.Course> courseSubscriber = new Subscriber<Student.Course>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Student.Course course) {
                LogUtil.i(course.toString());
            }
        };

        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Student.Course>>() {
                    @Override
                    public Observable<Student.Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(courseSubscriber);
    }

    /**
     * 连续地对数据序列的每一项应用一个函数，然后连续发射结果
     */
    private void rxScan() {
        Observable.just(1, 2, 3, 4, 5)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer sum, Integer item) {
                        return sum + item;
                    }
                }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                System.out.println("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Sequence complete.");
            }
        });
    }

}
