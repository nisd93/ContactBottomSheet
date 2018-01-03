package com.contactbottomsheet;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.contactbottomsheet.contactutils.Contact;
import com.contactbottomsheet.contactutils.RxContacts;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchContacts();
    }

    /*
       Fetch contacts
     */
    private void fetchContacts()
    {
        RxContacts
                .fetch(this)
                .filter(new Func1<Contact, Boolean>() {
                    @Override
                    public Boolean call (Contact contact) {
                        return contact.inVisibleGroup == 1;
                    }
                })
                .toSortedList(new Func2<Contact, Contact, Integer>() {
                    @Override
                    public Integer call (Contact lhs, Contact rhs) {
                        return lhs.displayName.compareTo(rhs.displayName);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contact>>() {
                    @Override
                    public void onCompleted () {}

                    @Override
                    public void onError (Throwable e) {}

                    @Override
                    public void onNext (List<Contact> contacts) {
                       init(contacts);
                    }
                });
    }
     /*
       Open bottom sheet after fetching contacts
     */
    private void init(List<Contact> contacts)
    {
        DataModelHolder.setData(contacts);
        BottomSheetDialogFragment bottomSheetDialogFragment = new ContactBottomSheetDialog();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    public enum DataModelHolder {
        INSTANCE;

        private List<Contact> mObjectList;

        public static boolean hasData() {
            return INSTANCE.mObjectList != null;
        }

        public static void setData(final List<Contact> objectList) {
            INSTANCE.mObjectList = objectList;
        }

        public static List<Contact> getData() {
            final List<Contact> retList = INSTANCE.mObjectList;
            INSTANCE.mObjectList = null;
            return retList;
        }
    }


}
