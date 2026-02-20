package com.example.ssb.piq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "piq_forms")
public class PiqForm {

    @Id
    private String id;

    // ✅ Linked to authenticated user (Supabase User ID)
    private String userId;
    private String userName;
    private String userEmail;

    // 1. Name
    private String fullName;

    // 2. Father's Name
    private String fathersName;

    // 3. Place of Residence
    private String placeOfResidence;

    // 4. Present Address
    private String presentAddress;
    private String presentAddressPopulation;

    // 5. Permanent Address
    private String permanentAddress;
    private String permanentAddressPopulation;

    // 6. Basic Details
    private String stateDistrict;
    private String religion;
    private String category; // SC/ST/OBC
    private String motherTongue;
    private String dateOfBirth;
    private String parentsAlive;
    private String ageAtParentsDeath;

    // 7. Occupation/Income Details
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyOccupation {
        private String relation; // Father, Mother, Guardian
        private String education;
        private String occupation;
        private String incomePerMonth;
    }
    private List<FamilyOccupation> familyDetails;

    // 8. Education Record
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationRecord {
        private String exam; // Matric, 10+2, Graduation, Professional
        private String year;
        private String divisionMarks;
        private String medium;
        private String boarderDayScholar;
    }
    private List<EducationRecord> educationDetails;

    // 9. Siblings
    private String numberOfBrothers;
    private String numberOfSisters;
    private String yourNumberInSiblings;

    // 10. Physical Stats
    private String ageYearsMonths;
    private String height;
    private String weight;

    // 11. Occupation
    private String presentOccupation;
    private String personalIncome;

    // 12. NCC
    private String nccTraining; // Yes/No
    private String nccTotalTraining;
    private String nccWing;
    private String nccDivision;
    private String nccCertificate;

    // 13. Activities
    private String gamesAndSports;
    private String hobbiesInterests;
    private String extraCurricular;
    private String positionOfResponsibility;
}
