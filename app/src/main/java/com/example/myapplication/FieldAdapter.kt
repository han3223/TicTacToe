package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FieldAdapter(
    private val field: Field,
    var isClicked: Boolean,
    private val onClick: (row: Int, col: Int) -> Unit
    ) : RecyclerView.Adapter<Vh>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(parent, onClick, isClicked)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val row = position / field.size
        val col = position % field.size
        holder.bind(field.get(row, col), row, col)
    }

    override fun getItemCount(): Int = field.size * field.size
}

class Vh(parent: ViewGroup, onClick: (row: Int, col: Int) -> Unit, isClicked: Boolean) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.field_cell, parent, false)
) {
    private val markTv = itemView.findViewById<TextView>(R.id.markTv)

    private var row: Int = -1
    private var col: Int = -1

    init {
        markTv.setOnClickListener {
            if (isClicked)
                onClick(row, col)
        }
    }

    fun bind(cell: Boolean?, row: Int, col: Int) {
        this.row = row
        this.col = col
        markTv.text = cell.toMark()
    }
}

