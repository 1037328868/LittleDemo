package com.sagee.zyq.testgreendao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.List;

import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.Person;
import greendao.PersonDao;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv_listView;
    private Button bt_insert;
    private Button bt_delete;
    private Button bt_update;
    private Button bt_query;
    private EditText et_name;
    private EditText et_age;
    private PersonDao mPersonDao;
    private List<Person> personList;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        initDB();
    }

    private void initDB() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "Person.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        mPersonDao = daoSession.getPersonDao();
    }

    private void initEvent() {
        bt_insert.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
        bt_update.setOnClickListener(this);
        bt_query.setOnClickListener(this);
    }

    private void initView() {
        lv_listView = (ListView) findViewById(R.id.lv_listView);
        bt_insert = (Button) findViewById(R.id.bt_insert);
        bt_delete = (Button) findViewById(R.id.bt_delete);
        bt_update = (Button) findViewById(R.id.bt_update);
        bt_query = (Button) findViewById(R.id.bt_query);
        et_name = (EditText) findViewById(R.id.et_name);
        et_age = (EditText) findViewById(R.id.et_age);

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (personList != null)
                    return personList.size();
                else
                    return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (getCount() != 0) {
                    Person person = personList.get(position);
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_main_listview, parent, false);
                    ((TextView) view.findViewById(R.id.tv_id)).setText(person.getId() + "");
                    ((TextView) view.findViewById(R.id.tv_name)).setText(person.getName());
                    ((TextView) view.findViewById(R.id.tv_age)).setText(person.getAge()+"");
                    return view;
                } else {
                    return null;
                }
            }
        };
        lv_listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_insert:
                Toast.makeText(this, "增", Toast.LENGTH_SHORT).show();
                insert();
                break;
            case R.id.bt_delete:
                Toast.makeText(this, "删", Toast.LENGTH_SHORT).show();
                delete();
                break;
            case R.id.bt_update:
                Toast.makeText(this, "改", Toast.LENGTH_SHORT).show();
                update();
                break;
            case R.id.bt_query:
                Toast.makeText(this, "查", Toast.LENGTH_SHORT).show();
                query();
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void delete() {
        // 查出跟输入匹配的然后删除
        String name = et_name.getText().toString().trim();
        int age = Integer.parseInt(et_age.getText().toString().trim());

        List<Person> list = mPersonDao.queryBuilder().list();
        for (Person person:list) {
            if (person.getName().equals(name)&&person.getAge()==age){
                mPersonDao.delete(person);
            }
        }

        query();
    }

    private void update() {
        // 查出跟输入匹配的然后修改
        String name = et_name.getText().toString().trim();
        int age = Integer.parseInt(et_age.getText().toString().trim());

        List<Person> list = mPersonDao.queryBuilder().list();
        for (Person person:list) {
            if (person.getName().equals(name)&&person.getAge()==age){
                person.setAge(0);
                person.setName("update");
                mPersonDao.insertOrReplace(person);
            }
        }
    }

    private void query() {
        //查询所有
        personList = mPersonDao.queryBuilder().list();
    }

    private void insert() {
        String name = et_name.getText().toString().trim();
        int age = Integer.parseInt(et_age.getText().toString().trim());
        //插入一条数据 ,id为Long型(不是long),传入null为自增
        mPersonDao.insert(new Person(null, name, age));
        query();
    }
}
