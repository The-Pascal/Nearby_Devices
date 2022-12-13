package com.solvabit.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bluetooth.communicator.BluetoothCommunicator
import com.bluetooth.communicator.Message
import com.bluetooth.communicator.Peer
import com.solvabit.myapplication.databinding.FragmentHomeBinding
import kotlin.random.Random

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    lateinit var bluetoothCommunicator: BluetoothCommunicator

    lateinit var binding: FragmentHomeBinding

    var connectedPeer: Peer?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val name = "test " + Random.nextInt(20)

        bluetoothCommunicator = BluetoothCommunicator(context, name , BluetoothCommunicator.STRATEGY_P2P_WITH_RECONNECTION)

        binding.textView.text = name

        return binding.root
    }
    override fun onStart() {
        super.onStart()

        initiateBluetooth()

        initiateClickListeners()

    }

    override fun onStop() {
        super.onStop()

        stopDiscovery()
    }

    private fun initiateClickListeners() {

        binding.startBtn.setOnClickListener {
            startDiscovery()
        }

        binding.stopBtn.setOnClickListener {
            stopDiscovery()
            binding.linearLayout.removeAllViews()
        }

        binding.sendBtn.setOnClickListener {
            connectedPeer?.let{ peer->
                bluetoothCommunicator.sendMessage(Message(context, "head", "madarchod kahi k yahan dekh", peer))
            }
        }

    }

    private fun stopDiscovery() {
//        bluetoothCommunicator.stopAdvertising(false)
        bluetoothCommunicator.stopDiscovery(false)
    }

    private fun startDiscovery() {
//        bluetoothCommunicator.startAdvertising()
        bluetoothCommunicator.startDiscovery()
    }

    private fun initiateBluetooth() {

        val bluetoothCallback = object : BluetoothCommunicator.Callback() {
            override fun onConnectionRequest(peer: Peer?) {
                super.onConnectionRequest(peer)

                Log.d(TAG, "onConnectionRequest: $peer")

                bluetoothCommunicator.acceptConnection(peer)

            }

            override fun onConnectionSuccess(peer: Peer?, source: Int) {
                super.onConnectionSuccess(peer, source)

                connectedPeer = peer

//                addMessage(peer.toString())

                Log.d(TAG, "onConnectionSuccess: $peer")

                Log.d(TAG, "onConnectionSuccess: $peer")
            }

            override fun onConnectionFailed(peer: Peer?, errorCode: Int) {
                super.onConnectionFailed(peer, errorCode)

                Toast.makeText(context, "Connection lost $peer", Toast.LENGTH_SHORT).show()

                Log.d(TAG, "onConnectionFailed: $peer $errorCode")
            }

            override fun onMessageReceived(message: Message?, source: Int) {
                super.onMessageReceived(message, source)

                message?.text?.let { addMessage(it) }

                Log.d(TAG, "onMessageReceived: $message")
            }

            override fun onPeerUpdated(peer: Peer?, newPeer: Peer?) {
                super.onPeerUpdated(peer, newPeer)

                connectToPeer(newPeer)

                Log.d(TAG, "onPeerUpdated: $peer")
            }

            override fun onPeerFound(peer: Peer?) {
                super.onPeerFound(peer)

                connectToPeer(peer)

                Log.d(TAG, "onPeerFound: $peer")
            }

            override fun onPeerLost(peer: Peer?) {
                super.onPeerLost(peer)

                Log.d(TAG, "onPeerLost: $peer")
            }
        }

        bluetoothCommunicator.addCallback(bluetoothCallback)

    }

    private fun addMessage(data: String) {
        val textView = TextView(context)
        textView.text = data
        textView.textSize = 16f
        binding.linearLayout.addView(textView)

    }

    private fun connectToPeer(peer: Peer?) {
        peer?.let {
            bluetoothCommunicator.connect(peer)
        }
    }
}