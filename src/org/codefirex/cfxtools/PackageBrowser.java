/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codefirex.cfxtools;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PackageBrowser extends ListActivity {
    static class MyPackageInfo {
        PackageInfo info;
        String label;
    }
    
    private PackageListAdapter mAdapter;
    private List<MyPackageInfo> mPackageInfoList = new ArrayList<MyPackageInfo>();
    private BroadcastReceiver mRegisteredReceiver;

    public class PackageListAdapter extends ArrayAdapter<MyPackageInfo> {

        public PackageListAdapter(Context context) {
            super(context, R.layout.package_list_item_simple);
            List<PackageInfo> pkgs = context.getPackageManager().getInstalledPackages(0);
            for (int i=0; i<pkgs.size(); i++) {
                MyPackageInfo info = new MyPackageInfo();
                info.info = pkgs.get(i);
                info.label = info.info.applicationInfo.loadLabel(getPackageManager()).toString();
                // only load launchable packages
                if(getPackageManager().getLaunchIntentForPackage(info.info.packageName) != null) {
                    mPackageInfoList.add(info);
                }
            }
            if (mPackageInfoList != null) {
                Collections.sort(mPackageInfoList, sDisplayNameComparator);
            }
            setSource(mPackageInfoList);
        }
    
        @Override
        public void bindView(View view, MyPackageInfo info) {
            ImageView icon = (ImageView)view.findViewById(R.id.icon);
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView description = (TextView)view.findViewById(R.id.description);
            icon.setImageDrawable(info.info.applicationInfo.loadIcon(getPackageManager()));
            name.setText(info.label);
            description.setText(info.info.packageName);
        }
    }

    /**
     * Receives notifications when applications are added/removed.
     */
    private class ApplicationsIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // todo: this is a bit brute force.  We should probably get the action and package name
            //       from the intent and just add to or delete from the mPackageInfoList
            setupAdapter();
        }
    }

    private final static Comparator<MyPackageInfo> sDisplayNameComparator
            = new Comparator<MyPackageInfo>() {
        public final int
        compare(MyPackageInfo a, MyPackageInfo b) {
            return collator.compare(a.label, b.label);
        }

        private final Collator   collator = Collator.getInstance();
    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setupAdapter();
        registerIntentReceivers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAdapter == null) {
            setupAdapter();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter = null;
        mPackageInfoList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRegisteredReceiver != null) {
            unregisterReceiver(mRegisteredReceiver);
        }
    }

    private void setupAdapter() {
        mAdapter = new PackageListAdapter(this);
        setListAdapter(mAdapter);
    }

    private void registerIntentReceivers() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        mRegisteredReceiver = new ApplicationsIntentReceiver();
        registerReceiver(mRegisteredReceiver, filter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        MyPackageInfo info =
            mAdapter.itemForPosition(position);
        if (info != null) {
            Intent returnIntent = new Intent();
            String myComponent = getPackageManager()
                    .getLaunchIntentForPackage(info.info.packageName).getComponent()
                    .flattenToString();
            returnIntent.putExtra("result", myComponent);
            returnIntent.putExtra("label", info.label);
            Log.i("CFXTools", "PackageBrowser result = " + myComponent);
            setResult(RESULT_OK, returnIntent);
        } else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }
        finish();
    }
}
