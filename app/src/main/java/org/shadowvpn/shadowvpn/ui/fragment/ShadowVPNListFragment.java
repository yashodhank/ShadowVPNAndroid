package org.shadowvpn.shadowvpn.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import org.shadowvpn.shadowvpn.R;
import org.shadowvpn.shadowvpn.model.ShadowVPNConfigure;
import org.shadowvpn.shadowvpn.ui.adapter.ShadowVPNConfigureAdapter;
import org.shadowvpn.shadowvpn.utils.ShadowVPNConfigureHelper;

public class ShadowVPNListFragment extends ListFragment {
    private static final int MENU_ID_STOP = 0x01;
    private static final int MENU_ID_EDIT = 0x02;
    private static final int MENU_ID_DELETE = 0x03;

    public static ShadowVPNListFragment newInstance() {
        ShadowVPNListFragment fragment = new ShadowVPNListFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    private ShadowVPNConfigureAdapter mShadowVPNConfigureAdapter;
    private IOnFragmentInteractionListener mListener;

    public ShadowVPNListFragment() {
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

        mShadowVPNConfigureAdapter = new ShadowVPNConfigureAdapter(getActivity(), ShadowVPNConfigureHelper.getAll(getActivity()));

        setListAdapter(mShadowVPNConfigureAdapter);

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

        ShadowVPNConfigure shadowVPNConfigure = mShadowVPNConfigureAdapter.getItem(position);

        if (mListener != null) {
            mListener.onShadowVPNConfigureClick(shadowVPNConfigure);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);

        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) contextMenuInfo;

        ShadowVPNConfigure configure = mShadowVPNConfigureAdapter.getItem(menuInfo.position);

        contextMenu.setHeaderTitle(configure.getTitle());

        if (configure.isSelected()) {
            contextMenu.add(Menu.NONE, ShadowVPNListFragment.MENU_ID_STOP, ShadowVPNListFragment.MENU_ID_STOP, R.string.context_menu_stop_configure);
        } else {
            contextMenu.add(Menu.NONE, ShadowVPNListFragment.MENU_ID_EDIT, ShadowVPNListFragment.MENU_ID_EDIT, R.string.context_menu_edit_configure);
            contextMenu.add(Menu.NONE, ShadowVPNListFragment.MENU_ID_DELETE, ShadowVPNListFragment.MENU_ID_DELETE, R.string.context_menu_delete_configure);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();

        ShadowVPNConfigure configure = mShadowVPNConfigureAdapter.getItem(menuInfo.position);

        switch (menuItem.getItemId()) {
            case ShadowVPNListFragment.MENU_ID_STOP:
                if (mListener != null) {
                    mListener.onShadowVPNConfigureStop(configure);
                }
                return true;
            case ShadowVPNListFragment.MENU_ID_EDIT:
                if (mListener != null) {
                    mListener.onShadowVPNConfigureEdit(configure);
                }
                return true;
            case ShadowVPNListFragment.MENU_ID_DELETE:
                if (mListener != null) {
                    mListener.onShadowVPNConfigureDelete(configure);
                }
                return true;
            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    public interface IOnFragmentInteractionListener {
        void onShadowVPNConfigureClick(ShadowVPNConfigure shadowVPNConfigure);

        void onShadowVPNConfigureStop(ShadowVPNConfigure shadowVPNConfigure);

        void onShadowVPNConfigureEdit(ShadowVPNConfigure shadowVPNConfigure);

        void onShadowVPNConfigureDelete(ShadowVPNConfigure shadowVPNConfigure);
    }
}