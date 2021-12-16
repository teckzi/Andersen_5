package tck.example.andersen_5.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import tck.example.andersen_5.R
import tck.example.andersen_5.classes.Contact
import tck.example.andersen_5.viewModel.ContactViewModel
import java.util.*

private const val TAG = "ContactFragment"
private const val CONTACT_ID = "contact_id"
class ContactFragment: Fragment(){
    interface Callback{
        fun onContactReplaced()
    }
    private lateinit var contact: Contact
    private lateinit var firstName: EditText
    private lateinit var secondName:EditText
    private lateinit var phoneNumber:EditText
    private lateinit var contactId: UUID
    private lateinit var saveButton: Button
    private var onSaveButtonPress:Boolean = false
    private var firstNameEmpty = false
    private var secondNameEmpty = false
    private var phoneNumberEmpty = false

    private val contactViewModel: ContactViewModel by lazy {
        ViewModelProvider(this).get(ContactViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as Callback?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contact = Contact()
        contactId = arguments?.getSerializable(CONTACT_ID) as UUID
        contactViewModel.loadContact(contactId)
        Log.d(TAG, "contactId $contactId")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_page, container, false)
        firstName = view.findViewById(R.id.firstName)
        secondName = view.findViewById(R.id.secondName)
        phoneNumber = view.findViewById(R.id.number)
        saveButton = view.findViewById(R.id.saveButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactId = arguments?.getSerializable(CONTACT_ID) as UUID
        contactViewModel.loadContact(contactId)
        contactViewModel.contactLiveData.observe(
            viewLifecycleOwner,
            { contact ->
                contact?.let {
                    this.contact = contact
                    updateUI()
                }
            })

        saveButton.setOnClickListener {
            onSaveButtonPress = true
            contactViewModel.updateContact(contact)
            callback?.onContactReplaced()
        }
    }

    override fun onStart() {
        super.onStart()
        firstName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                contact.firstName = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        secondName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                contact.secondName = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        phoneNumber.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                contact.phoneNumber = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.contact_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.delete_contact -> {
                contactViewModel.deleteContact(contactId)
                callback?.onContactReplaced()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(){
        firstName.setText(contact.firstName)
        secondName.setText(contact.secondName)
        phoneNumber.setText(contact.phoneNumber)

        firstNameEmpty = firstName.text.toString() == ""
        secondNameEmpty = secondName.text.toString() == ""
        phoneNumberEmpty = phoneNumber.text.toString() == ""
    }

    override fun onDetach() {
        super.onDetach()
        if (!onSaveButtonPress) {
            if (firstNameEmpty && secondNameEmpty && phoneNumberEmpty) contactViewModel.deleteContact(contactId)
        }
        callback = null
    }

    companion object{
        fun newInstance(contactId: UUID):ContactFragment{
            val args = Bundle().apply {
                putSerializable(CONTACT_ID,contactId)
            }
            return ContactFragment().apply {
                arguments = args
            }
        }
        var callback:Callback? = null
    }
}