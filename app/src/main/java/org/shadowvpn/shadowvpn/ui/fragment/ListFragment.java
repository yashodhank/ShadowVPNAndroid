package org.shadowvpn.shadowvpn.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import org.shadowvpn.shadowvpn.R;
import org.shadowvpn.shadowvpn.model.VpnConfigure;
import org.shadowvpn.shadowvpn.ui.adapter.ConfigureAdapter;
import org.shadowvpn.shadowvpn.utils.ConfigureHelper;

public class ListFragment extends android.support.v4.app.ListFragment {
    private static final int MENU_ID_STOP = 0x01;
    private static final int MENU_ID_EDIT = 0x02;
    private static final int MENU_ID_DELETE = 0x03;

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    private ConfigureAdapter mConfigureAdapter;
    private IOnFragmentInteractionListener mListener;

    public ListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (IOnFragmentInteractionListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mConfigureAdapter = new ConfigureAdapter(getActivity(), ConfigureHelper.getAll(getActivity()));

        setListAdapter(mConfigureAdapter);

        registerForContextMenu(getListView());
    }

    @Override
    public void onDestroyView() {
        unregisterForContextMenu(getListView());
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        VpnConfigure vpnConfigure = mConfigureAdapter.getItem(position);

        if (mListener != null) {
            mListener.onShadowVPNConfigureClick(vpnConfigure);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);

        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) contextMenuInfo;

        VpnConfigure configure = mConfigureAdapter.getItem(menuInfo.position);

        contextMenu.setHeaderTitle(configure.getTitle());

        if (configure.isSelected()) {
            contextMenu.add(Menu.NONE, ListFragment.MENU_ID_STOP, ListFragment.MENU_ID_STOP, R.string.context_menu_stop_configure);
        } else {
            contextMenu.add(Menu.NONE, ListFragment.MENU_ID_EDIT, ListFragment.MENU_ID_EDIT, R.string.context_menu_edit_configure);
            contextMenu.add(Menu.NONE, ListFragment.MENU_ID_DELETE, ListFragment.MENU_ID_DELETE, R.string.context_menu_delete_configure);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();

        VpnConfigure configure = mConfigureAdapter.getItem(menuInfo.position);

        switch (menuItem.getItemId()) {
            case ListFragment.MENU_ID_STOP:
                if (mListener != null) {
                    mListener.onShadowVPNConfigureStop(configure);
                }
                return true;
            case ListFragment.MENU_ID_EDIT:
                if (mListener != null) {
                    mListener.onShadowVPNConfigureEdit(configure);
                }
                return true;
            case ListFragment.MENU_ID_DELETE:
                if (mListener != null) {
                    mListener.onShadowVPNConfigureDelete(configure);
                }
                return true;
            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    public interface IOnFragmentInteractionListener {
        void onShadowVPNConfigureClick(VpnConfigure vpnConfigure);

        void onShadowVPNConfigureStop(VpnConfigure vpnConfigure);

        void onShadowVPNConfigureEdit(VpnConfigure vpnConfigure);

        void onShadowVPNConfigureDelete(VpnConfigure vpnConfigure);
    }
}