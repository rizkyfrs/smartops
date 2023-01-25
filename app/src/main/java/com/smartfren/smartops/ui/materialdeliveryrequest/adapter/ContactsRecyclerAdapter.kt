package com.smartfren.smartops.ui.materialdeliveryrequest.adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Contact

class ContactsRecyclerAdapter(private val context: Context, private val contacts: MutableList<Contact>) :
    RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactViewHolder(inflater, parent)
    }

    inner class ContactViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_schedule_list_detail, parent, false)) {
        private val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        private val itemLayout: ConstraintLayout = itemView.findViewById(R.id.lay)
        private val textName: TextView = itemView.findViewById(R.id.tvActRegionNmSch)
        private val textAge: TextView = itemView.findViewById(R.id.tvActNumSch)

        fun bind(contact: Contact) {
            textAge.text = contact.product_number
            textName.text = contact.vendor
            btnDelete.setOnClickListener { removeContact() }
            itemLayout.setOnClickListener {
                showDialogAddMDRItem(
                    contact.product_number,
                    contact.faulty_model_sn,
                    contact.faulty_info,
                    contact.vendor,
                    contact.ne,
                    contact.equpment_name,
                    contact.module_name
                )
            }
        }

        private fun removeContact() {
            contacts.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

    }

    fun addContact(contact: Contact) {
        contacts.add(contact)
        notifyItemInserted(itemCount)
    }

    fun clearAll() {
        contacts.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount() = contacts.size


    private fun showDialogAddMDRItem(pn: String?, fmsn: String?, fi: String?, vendor: String?, ne: String?, en: String?, mn: String?) {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pop_up_mdr_item_add)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val btnAddMDRItem = dialog.findViewById(R.id.btnAddMDRItem) as Button
        val btnClose = dialog.findViewById(R.id.btnCloseAddMDRItem) as ImageView
        val etAddMDRProductNumber = dialog.findViewById(R.id.etAddMDRProductNumber) as EditText
        val etFaultModuleSN = dialog.findViewById(R.id.etAddMDRFaultModuleSN) as AppCompatEditText
        val etAddMDRFaultInfo = dialog.findViewById(R.id.etAddMDRFaultInfo) as EditText
        val etADdMDRVendor = dialog.findViewById(R.id.etADdMDRVendor) as EditText
        val etADdMDRNE = dialog.findViewById(R.id.etADdMDRNE) as EditText
        val etADdMDREquipmentName = dialog.findViewById(R.id.etADdMDREquipmentName) as EditText
        val etADdMDRModuleName = dialog.findViewById(R.id.etADdMDRModuleName) as EditText

        btnAddMDRItem.text = context.getString(R.string.edit)

        etAddMDRProductNumber.isEnabled = false
        etFaultModuleSN.isEnabled = false
        etAddMDRFaultInfo.isEnabled = false
        etADdMDRVendor.isEnabled = false
        etADdMDRNE.isEnabled = false
        etADdMDREquipmentName.isEnabled = false
        etADdMDRModuleName.isEnabled = false

        etAddMDRProductNumber.setText(pn)
        etFaultModuleSN.setText(fmsn)
        etAddMDRFaultInfo.setText(fi)
        etADdMDRVendor.setText(vendor)
        etADdMDRNE.setText(ne)
        etADdMDREquipmentName.setText(en)
        etADdMDRModuleName.setText(mn)

        dialog.show()

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnAddMDRItem.setOnClickListener {
            val model = Contact(
                etAddMDRProductNumber.text.toString(), etFaultModuleSN.text.toString(), etAddMDRFaultInfo.text.toString(),
                etADdMDRVendor.text.toString(), etADdMDRNE.text.toString(), etADdMDREquipmentName.text.toString(), etADdMDRModuleName.text.toString(),
                ""
            )
            addContact(model)

            dialog.dismiss()
        }
    }
}