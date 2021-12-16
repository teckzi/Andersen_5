package tck.example.andersen_5.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tck.example.andersen_5.classes.Contact
import tck.example.andersen_5.R
import tck.example.andersen_5.viewModel.ContactListViewModel
import java.util.*

private const val TAG = "ContactListFragment"
class ContactListFragment: Fragment() {
    interface Callbacks{
        fun onContactSelected(contactId: UUID)
    }

    private lateinit var contactRecyclerView: RecyclerView
    private var adapter: ContactsListAdapter? = ContactsListAdapter(emptyList())
    private var emptyAdapter:EmptyAdapter = EmptyAdapter()
    private val startingContactsList = listOf(Contact(firstName = "1st Contact Name", secondName = "1st Contact SecondName", phoneNumber = "+72102102404"),
        Contact(firstName = "2st Contact Name", secondName = "2st Contact SecondName", phoneNumber = "+41291212489"),
        Contact(firstName = "3st Contact Name", secondName = "4st Contact SecondName", phoneNumber = "+21491287494"),
        Contact(firstName = "4st Contact Name", secondName = "4st Contact SecondName", phoneNumber = "+10102849479")
    )
    private val contactListViewModel: ContactListViewModel by lazy {
        ViewModelProvider(this).get(ContactListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        contactRecyclerView = view.findViewById(R.id.contacts_recycler_view)
        contactRecyclerView.layoutManager = LinearLayoutManager(context)
        contactRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactListViewModel.contactListLiveData.observe(viewLifecycleOwner, { contacts ->
                contacts.let {
                    Log.d(TAG, "Got contacts ${contacts.size}")
                    updateUI(contacts)} })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(contacts: List<Contact>){
        emptyAdapter = EmptyAdapter()
        adapter = ContactsListAdapter(contacts)
        if (contacts.isEmpty()) contactRecyclerView.adapter = emptyAdapter
        else contactRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.contact_list_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.new_contact -> {
                val contact = Contact()
                contactListViewModel.addContact(contact)
                callbacks?.onContactSelected(contact.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class EmptyHolder(view: View): RecyclerView.ViewHolder(view){
        private val emptyListButton: Button = itemView.findViewById(R.id.emptyListButton)
        init {
            emptyListButton.setOnClickListener {
                val contact = Contact()
                contactListViewModel.addContact(contact)
                callbacks?.onContactSelected(contact.id)
            }
        }
    }

    private inner class EmptyAdapter : RecyclerView.Adapter<EmptyHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_empty,parent,false)
            return EmptyHolder(view)
        }
        override fun onBindViewHolder(holder: EmptyHolder, position: Int) {}
        override fun getItemCount(): Int = 1
    }

    private inner class ContactHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var contact:Contact
        private val firstNameTextView: TextView = itemView.findViewById(R.id.contactFirstname)
        private val secondNameTextView: TextView = itemView.findViewById(R.id.contactSecondName)
        private val phoneNumberTextView: TextView = itemView.findViewById(R.id.contactPhoneNumber)
        init {
            itemView.setOnClickListener(this)
        }
        fun bind(contact: Contact){
            this.contact = contact
            firstNameTextView.text = this.contact.firstName
            secondNameTextView.text = this.contact.secondName
            phoneNumberTextView.text = this.contact.phoneNumber
        }

        override fun onClick(view: View?) {
            callbacks?.onContactSelected(contact.id)
        }
    }

    private inner class ContactsListAdapter (var contact:List<Contact>): RecyclerView.Adapter<ContactHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
            return ContactHolder(view)
        }
        override fun onBindViewHolder(holder: ContactHolder, position: Int) {
            val contact = contact[position]
            holder.bind(contact)
        }
        override fun getItemCount(): Int {
            return contact.size
        }
    }

    companion object{
        fun newInstance(): ContactListFragment {
            return ContactListFragment()
        }
        var callbacks:Callbacks? = null
    }
}