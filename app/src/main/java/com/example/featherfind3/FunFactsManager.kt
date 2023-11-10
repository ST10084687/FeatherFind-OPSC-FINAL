package com.example.featherfind3

class FunFactsManager {
    private var feathers: Int = 0

    fun getFeathers(): Int {
        return feathers
    }

    fun earnFeather() {
        feathers++
    }

    fun getRandomFunFact(): String {
        val funFacts = listOf(
            "Hummingbirds can fly backward.",
            "Owls can rotate their heads 270 degrees.",
            "Penguins mate for life.",
            "Flamingos are born with gray feathers and turn pink due to their diet.",
            "The wandering albatross has the largest wingspan of any living bird, up to 12 feet.",
            "Crows are known to hold grudges against humans who have wronged them.",
            "The song of a canary can be affected by its surroundings, making them excellent mimics.",
            "Kiwis are flightless birds and have tiny, vestigial wings.",
            "The male frigatebird's red throat pouch inflates during courtship to attract females.",
            "Parrots can imitate human speech and other sounds.",
            "The national bird of the United States, the bald eagle, is not actually bald.",
            "The harpy eagle has talons as long as a grizzly bear's claws.",
            "Puffins can hold multiple fish in their beaks at once.",
            "The lyrebird is known for its impressive ability to mimic natural and artificial sounds.",
            "Peacocks are known for their colorful and extravagant plumage.",
            "Birds are descendants of theropod dinosaurs, making them distant relatives of T. rex.",
            "The secretary bird is a bird of prey with long legs that resembles a secretary's quill in flight.",
            "Toucans have large, colorful bills that make them instantly recognizable.",
            "Some species of woodpeckers drum on trees to communicate and establish territory.",
            "The bar-tailed godwit holds the record for the longest non-stop flight by a bird, covering 7,145 miles.",
            "The peregrine falcon is the fastest bird, capable of speeds over 240 mph in a stoop.",
            "Male seagulls often bring gifts to their mates as part of their courtship ritual.",
            "The Andean condor is one of the world's heaviest flying birds, with a wingspan of up to 11 feet.",
            "Cockatoos are known for their distinctive crests and playful personalities.",
            "The male superb lyrebird displays its long tail feathers in an elaborate courtship dance.",
            "Cassowaries are large, flightless birds known for their sharp, dagger-like claws.",
            "Flamingos feed with their heads upside down in the water to filter out algae and small invertebrates.",
            "Terns are migratory birds that travel thousands of miles between their breeding and wintering grounds.",
            "Albatrosses can fly for days without flapping their wings, using dynamic soaring.",
            "Vultures have a remarkable sense of smell to locate carrion from high in the sky.",
            "Ostriches are the largest and heaviest birds in the world.",
            "Swans are known for their grace and are often associated with romance.",
            "Mockingbirds can imitate the sounds of other birds and even mechanical noises.",
            "The Atlantic puffin's beak changes color during the breeding season.",
            "Kookaburras have a distinctive laughing call that sounds like human laughter.",
            "Some species of parrots can live for over 80 years.",
            "The kea, a parrot from New Zealand, is known for its playful and mischievous behavior.",
            "Cranes are known for their elaborate courtship dances and displays.",
            "The red-winged blackbird is known for its striking red and yellow wing patches.",
            "Bowerbirds construct intricate bowers and decorate them with colorful objects to attract mates.",
            "The marabou stork has a wingspan of up to 10 feet and a bare, pink head.",
            "Bald ibises are critically endangered and have a distinct bald head.",
            "Peregrine falcons are used in falconry due to their speed and hunting abilities.",
            "The hoatzin is sometimes called the stinkbird due to its foul-smelling digestive system.",
            "Kestrels are known for their hovering flight while hunting for prey.",
            "Snowy owls are well adapted to cold environments and have thick feathering on their legs and feet.",
            "Ostriches have the largest eyes of any land animal.",
            "Many species of birds are monogamous and form long-term partnerships with their mates.",
            "Birds have hollow bones to reduce their weight for flight.",
            "Some birds, like the lyrebird, can imitate chainsaws and camera shutters.",
            "Parrots can use tools and solve puzzles to obtain food.",
            "Peregrine falcons have been known to catch prey mid-air in breathtaking aerial attacks."
        )

        val randomIndex = (0 until funFacts.size).random()
        return funFacts[randomIndex]
    }
}