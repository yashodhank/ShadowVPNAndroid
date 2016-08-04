package org.shadowvpn.shadowvpn.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.shadowvpn.shadowvpn.R;
import org.shadowvpn.shadowvpn.model.VpnConfigure;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ConfigureAdapter extends BaseAdapter implements RealmChangeListener {
    private Context mContext;
    private RealmResults<VpnConfigure> mVpnConfigures;

    public ConfigureAdapter(Context context, @NonNull RealmResults<VpnConfigure> vpnConfigureRealmResults) {
        mContext = context;
        mVpnConfigures = vpnConfigureRealmResults;

        Realm.getInstance(mContext).addChangeListener(this);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mVpnConfigures.size();
    }

    @Override
    public VpnConfigure getItem(int position) {
        return mVpnConfigures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    @Override
    public View getView(int pPosition, View convertView, ViewGroup parent) {
        View layout;

        if (convertView == null) {
            layout = LayoutInflater.from(mContext).inflate(R.layout.list_item_configure, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) layout.findViewById(R.id.icon);
            viewHolder.title = (TextView) layout.findViewById(R.id.title);
            viewHolder.summary = (TextView) layout.findViewById(R.id.summary);

            layout.setTag(viewHolder);
        } else {
            layout = convertView;
        }

        ViewHolder viewHolder = (ViewHolder) layout.getTag();
        VpnConfigure configure = getItem(pPosition);

        viewHolder.icon.setImageResource(configure.isSelected() ? R.drawable.ic_vpn_connected : R.drawable.ic_vpn_unconnected);
        viewHolder.title.setText(configure.getTitle());
        viewHolder.summary.setText(configure.getServerIP());

        return layout;
    }

    private static class ViewHolder {
        public ImageView icon;

        public TextView title;

        public TextView summary;
    }
}
