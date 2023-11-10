package com.example.featherfind3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BirdAdapter (private val birdDataList: List<BirdDataItemItem>) :
    RecyclerView.Adapter<BirdAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val speciesCodeTextView: TextView = itemView.findViewById(R.id.speciesCodeTextView)
        val commonNameTextView: TextView = itemView.findViewById(R.id.commonNameTextView)
        val scientificNameTextView: TextView = itemView.findViewById(R.id.scientificNameTextView)
        val predictionYearTextView: TextView = itemView.findViewById(R.id.predictionYearTextView)
        val regionTypeTextView: TextView = itemView.findViewById(R.id.regionTypeTextView)
        val regionCodeTextView: TextView = itemView.findViewById(R.id.regionCodeTextView)
        val regionNameTextView: TextView = itemView.findViewById(R.id.regionNameTextView)
        val seasonTextView: TextView = itemView.findViewById(R.id.seasonTextView)
        val startDateTextView: TextView = itemView.findViewById(R.id.startDateTextView)
        val endDateTextView: TextView = itemView.findViewById(R.id.endDateTextView)
        val abundanceMeanTextView: TextView = itemView.findViewById(R.id.abundanceMeanTextView)
        val totalPopPercentTextView: TextView = itemView.findViewById(R.id.totalPopPercentTextView)
        val rangePercentOccupiedTextView: TextView = itemView.findViewById(R.id.rangePercentOccupiedTextView)
        val rangeTotalPercentTextView: TextView = itemView.findViewById(R.id.rangeTotalPercentTextView)
        val rangeDaysOccupationTextView: TextView = itemView.findViewById(R.id.rangeDaysOccupationTextView)



    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_bird, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val birdData = birdDataList[position]
        holder.speciesCodeTextView.text = "Species Code: ${birdData.species_code}"
        holder.commonNameTextView.text = "Common Name: ${birdData.common_name}"
        holder.scientificNameTextView.text = "Scientific Name: ${birdData.scientific_name}"
        holder.predictionYearTextView.text = "Prediction Year: ${birdData.prediction_year}"
        holder.regionTypeTextView.text = "Region Type: ${birdData.region_type}"
        holder.regionCodeTextView.text = "Region Code: ${birdData.region_code}"
        holder.regionNameTextView.text = "Region Name: ${birdData.region_name}"
        holder.seasonTextView.text = "Season: ${birdData.season}"
        holder.startDateTextView.text = "Start Date: ${birdData.start_date}"
        holder.endDateTextView.text = "End Date: ${birdData.end_date}"
        holder.abundanceMeanTextView.text = "Abundance Mean: ${birdData.abundance_mean}"
        holder.totalPopPercentTextView.text = "Total Pop Percent: ${birdData.total_pop_percent}"
        holder.rangePercentOccupiedTextView.text = "Range Percent Occupied: ${birdData.range_percent_occupied}"
        holder.rangeTotalPercentTextView.text = "Range Total Percent: ${birdData.range_total_percent}"
        holder.rangeDaysOccupationTextView.text = "Range Days Occupied: ${birdData.range_days_occupation}"

    }

    override fun getItemCount(): Int {
        return birdDataList.size
    }

}