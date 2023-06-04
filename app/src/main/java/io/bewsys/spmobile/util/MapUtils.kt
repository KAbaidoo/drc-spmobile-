package io.bewsys.spmobile.util


object MapUtil {
    val intMappings = mapOf<String, Int>(
//        "Male" to "M",
//        "Female" to "F",

        "Urban" to 157,
        "Urban-rural" to 158,
        "Rural" to 159,

// migration status
        "Resident" to 1,
        "Repatriated" to 2,
        "Displaced" to 3,
        "Refugee" to 4,
        "Returned" to 5,


//        family bond
        "Head of household" to 6,
        "Wife or Husband" to 7,
        "Son/Daughter" to 8,
        "Son-in-law/Daughter-in-law" to 9,
        "Grandson/Granddaughter" to 10,
        "Father/Mother" to 11,
        "Parents-in-law" to 12,
        "Brother/Sister" to 13,
        "Nephew/Niece" to 14,
        "Nephew/Niece by marriage" to 15,
        "Other relative" to 16,
        "Adopted child/Cared for" to 17,
        "Unrelated" to 18,
        "Don\'t know" to 19,

//        marital status
        "Bachelor" to 20,
        "Married monogamously" to 21,
        "Married polygamous" to 22,
        "Widow /widower" to 23,
        "Divorce/separated" to 24,
        "Common-law union" to 25,

        "Yes" to 1,
        "No" to 0,

//level of education

        "Pre-school" to 26,
        "Primary" to 27,
        "Secondary" to 28,
        "Superior" to 29,
        "Technical/Vocational training" to 30,
        "None" to 31,
        "Not concerned" to 32,
        "Don\'t know level of education" to 33,

        //        school attendence
        "Pre-school" to 34,
        "1st year" to 35,
        "Grade 2" to 36,
        "Grade 3" to 37,
        "Grade 4" to 38,
        "Grade 5" to 39,
        "Grade 6" to 40,
        "Grade 7" to 41,
        "Grade 8" to 42,
        "Secondary" to 43,
        "Superior" to 45,
        "School dropout" to 45,
        "Not concerned" to 46,
        "Studies completed" to 47,
        "Don\'t know" to 48,

        //   disability
        "Motor" to 49,
        "Visual" to 50,
        "Auditory" to 51,
        "Mental" to 52,
        "Chronic disease" to 53,
        "Multiple disabilities" to 54,
        "None" to 55,

        //socio-professional category
        "Senior Management/Executive" to 56,
        "Employee/Skilled or semi-skilled worker" to 57,
        "Manual worker/unskilled worker" to 58,
        "Employer" to 59,
        "Self-employed" to 60,
        "Family helper" to 61,
        "Paid or unpaid apprentice" to 62,
        "Unemployed" to 63,
        "Inactive" to 64,

// sector of work
        "Formal sector (public or private)" to 65,
        "Non-agricultural infromal" to 66,
        "Agricultural informal" to 67,
        "No concerns" to 68,

//occupancy status of household
        "Owner" to 69,
        "Housed by relative/friend" to 70,
        "Tenant" to 71,
        "Housed by employer" to 72,
        "Site guard" to 73,
        "Sub-housed" to 74,


// exterior walls
        "No wall" to 76,
        "Bamboo/Cane/Palm/Trunk" to 77,
        "Earth" to 78,
        "Bamboo with mud" to 79,
        "Stones with mud" to 80,
        "Adobe not covered" to 81,
        "Plywood" to 82,
        "Cardboard" to 83,
        "Salvage timber" to 84,
        "Cement" to 85,
        "Stones with lime/cement timber" to 86,
        "Bricks" to 87,
        "Cement blocks" to 88,
        "Adobe covered" to 89,
        "Wooden board/Shingles" to 90,

// soil material
        "Earth/Sand" to 92,
        "Dung" to 93,
        "Wooden boards" to 94,
        "Fins/bamboo" to 95,
        "Parquet or waxed wood" to 96,
        "Vynille/Asphalt strips" to 97,
        "Tiling" to 98,
        "Cement" to 99,
        "Carpet" to 100,

//fuel type used for cooking
        "Electricity" to 102,
        "Liquefied Propane Gas (LPG)" to 103,
        "Natural gas" to 104,
        "Biogas" to 105,
        "Kerosene" to 106,
        "Coal/Ignite" to 107,
        "Charcoal" to 108,
        "Wood" to 109,
        "Straw/Branches/Grass" to 110,
        "Agricultural residues" to 111,
        "Dung" to 112,
        "No meal prepared in the household" to 113,

// drinking water
        "Tap in the dwelling" to 115,
        "Faucet in the yard/plot" to 116,
        "Public tap/standpipe" to 117,
        "Tap at the neighbor\'s house" to 118,
        "Pump wells or boreholes" to 119,
        "Protected well" to 120,
        "Unprotected well" to 121,
        "Protected source" to 122,
        "Unprotected source" to 123,
        "Rainwater" to 124,
        "Tanker" to 125,
        "Cart with small tank/barrel" to 126,
        "Surface water (ver/dam/lake/pond/canal)" to 127,
        "Bottled water" to 128,

//toilet
        "Flushing/flushing" to 130,
        "Flush connected to a sewer/septic tank/cesspool system" to 131,
        "Cesspool" to 132,
        "Composting toilets" to 133,
        "Bucket/tinette" to 134,
        "Suspended toilets/latrines" to 135,
        "No toilets/nature" to 136,

//waste disposal
        "Private or public organized service" to 137,
        "Incineration" to 138,
        "Landfill" to 139,
        "Public highway" to 140,
        "Watercourses" to 141,
        "Wild dump" to 142,
        "Compost or manure" to 143,


// place to wash hands
        "Fixed place (Washbasin/tap): water and soap available" to 145,
        "Fixed place (Washbasin/Faucet): water available and no soap" to 146,
        "Fixed place (Washbasin/Faucet): water not available and soap available" to 147,
        "Mobile device (water and soap available)" to 148,
        "Mobile device (water available and no soap)" to 149,
        "Mobile device (water not available and soap available)" to 150,
        "No permission" to 151,
        "No venue" to 152,


// area of residence
        "Urbain" to 157,
        "Urbano-rural" to 158,
        "Rural" to 159,

//        migration status
        "Résident" to 1,
        "Rapatrié" to 2,
        "Déplacé" to 3,
        "Réfugié" to 4,
        "Renvoyé" to 5,

//        family bond
        "Chef de ménage" to 6,
        "Femme ou mari" to 7,
        "Fils/fille" to 8,
        "Gendre/belle-fille" to 9,
        "Petit fils/fille" to 10,
        "Père/mère" to 11,
        "Beaux parents" to 12,
        "Frère/sœur" to 13,
        "Neveu/nièce" to 14,
        "Neveu/nièce par alliance" to 15,
        "Autres parents" to 16,
        "Enfant adopté/gardé/de la femme/du mari" to 17,
        "Sans parenté" to 18,
        "Ne sait pas" to 19,

//        marital status
        "Célibataire" to 20,
        "Marié(e) monogame" to 21,
        "Marié(e) polygame" to 22,
        "Veuf/veuve" to 23,
        "Divorcé(e)/séparé(e)" to 24,
        "Union libre" to 25,

        "Oui" to 1,
        "Non" to 2,

//level of education
        "Pré-scolaire" to 26,
        "Primaire" to 27,
        "Secondaire" to 28,
        "Supérieur" to 29,
        "Formation technique ou professionelle" to 30,
        "Aucun" to 31,
        "Non concerné;" to 32,
        "Ne sait pas niveau d\'instruction" to 33,


        "Pré-scolaire" to 34,
        "1ère année" to 35,
        "2e année" to 36,
        "3e année" to 37,
        "4e année" to 38,
        "5e année" to 39,
        "6e année" to 40,
        "7e année" to 41,
        "8e année" to 42,
        "Secondaire" to 43,
        "Supérieur" to 44,
        "Abandon scolaire" to 45,
        "Non concerné" to 46,
        "Etudes terminées" to 47,
        "Ne sait pas" to 48,

//        disabillity

        "Moteur" to 49,
        "Visuel" to 50,
        "Auditif" to 51,
        "Mental" to 52,
        "Maladie chronique" to 53,
        "Handicaps multiples" to 54,
        "Aucune" to 55,

        //socio-professional category
        "Cadre supérieur / Cadre de direction" to 56,
        "Employé/travailleur qualifié ou semi-qualifié" to 57,
        "Ouvrier/travailleur non qualifie" to 58,
        "Employeur / Patron" to 59,
        "Indépendant" to 60,
        "Aide familiale" to 61,
        "Apprentis payé ou non payé" to 62,
        "Chomeur" to 63,
        "Inactif" to 64,

//sector of work
        "Secteur formel (public ou privé)" to 65,
        "Informel non agricole" to 66,
        "Informel agricole" to 67,
        "Non concerne" to 68,

// occupancy status
        "Propriétaire" to 69,
        "Logé par un parent/ami" to 70,
        "Locataire" to 71,
        "Logé par l\'employeur" to 72,
        "Garde-chantier" to 73,
        "Sous-logé" to 74,


//main material of exterior walls
        "Pas de mur" to 76,
        "Bambou/Cane/Palme/Tronc" to 77,
        "Terre" to 78,
        "Bambou avec boue" to 79,
        "Pierres avec boue" to 80,
        "Adobe non recouvert;" to 81,
        "Contre-plaqué" to 82,
        "Carton" to 83,
        "Bois de recuperation" to 84,
        "Ciment" to 85,
        "Pierres avec chaux/ciment" to 86,
        "Briques" to 87,
        "Blocs de ciment" to 88,
        "Adobe recouvert" to 89,
        "Planche en bois/shingles" to 90,


// soil material
        "Terre/Sable" to 92,
        "Bouse" to 93,
        "Planches en bois" to 94,
        "Palmes/bambou" to 95,
        "Parquet ou bois ciré" to 96,
        "Bandes de vynille/asphalte" to 97,
        "Carrelage" to 98,
        "Ciment" to 99,
        "Moquette" to 100,


//fuel type used for cooking
        "Electricité" to 102,
        "Gaz Propane Liquéfié (GPL)" to 103,
        "Gaz naturel" to 104,
        "Biogaz" to 105,
        "Kerosene" to 106,
        "Charbon/lignite" to 107,
        "Charbon de bois" to 108,
        "Bois" to 109,
        "Paille/branchages/herbes" to 110,
        "Résidus agricoles" to 111,
        "Bouse" to 112,
        "Pas de repas préparé dans le menage" to 113,


//        source of drinking water
        "Robinet dans le logement" to 115,
        "Robinet dans la cour/parcelle" to 116,
        "Robinet public/brone fontaine" to 117,
        "Robinet chez le voisin" to 118,
        "Puits à pompe ou forage" to 119,
        "Puits protégé" to 120,
        "Puits non protégé" to 121,
        "Source protégée" to 122,
        "Source non protégée" to 123,
        "Eau de pluie" to 124,
        "Camion citerne" to 125,
        "Charrette avec petite citerne/tonneau" to 126,
        "Eau de surface (fleuve/barrage/lac/mare/canal irrigation)" to 127,
        "Eau en bouteille" to 128,


//toilet
        "Chasse d\'eau/chasse manuelle" to 130,
        "Chasse d\'eau connectée à un systeme d\'egout/fosse septique/fosse d\'aisances" to 131,
        "Fosse d\'aisances" to 132,
        "Toilettes à compostage" to 133,
        "Seau/Tinette" to 134,
        "Toilettes/latrines suspendues" to 135,
        "Pas de toilettes/nature" to 136,

//        waster disposal
        "Service organisé privé ou public" to 137,
        "Incinération" to 138,
        "Enfouissement" to 139,
        "Enfouissement" to 140,
        "Cours d\'eau" to 141,
        "Depotoire sauvage" to 142,
        "Compost ou fumier" to 143,


        // place to wash hands
        "Lieu fixe (Lavabo/Robinet): eau et savon disponible" to 145,
        "Lieu fixe (Lavabo/Robinet): eau disponible et pas de savon" to 146,
        "Lieu fixe (Lavabo/Robinet): eau non disponible et savon disponible" to 147,
        "Dispositif mobile (eau et savon disponibles)" to 148,
        "Dispositif mobile (eau disponible et pas de savon)" to 149,
        "Dispositif mobile (eau non disponible et savon disponible)" to 150,
        "Pas de permission" to 151,
        "Pas de lieu" to 152,
    )

    val stringMapping: Map<String, String> = mapOf(
        "Male" to "M",
        "Female" to "F",
        "Homme" to "M",
        "Femme" to "F",

        "Yes" to "yes",
        "No" to "no",
        "Oui" to "yes",
        "Non" to "no",




    )
}

