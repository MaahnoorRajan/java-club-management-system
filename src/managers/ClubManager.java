package managers;

import exceptions.ClubNotFoundException;
import exceptions.DuplicateMemberException;
import exceptions.InvalidClubDataException;
import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClubManager {

    private List<Club> clubs;

    private List<StudentMember> members;

    private List<Activity> allActivities;

    private Map<String, Club> clubMap;

    private Map<String, StudentMember> memberMap;

    private int clubCounter;
    private int memberCounter;
    private int activityCounter;


    public ClubManager() {
        this.clubs          = new ArrayList<>();
        this.members        = new ArrayList<>();
        this.allActivities  = new ArrayList<>();
        this.clubMap        = new HashMap<>();
        this.memberMap      = new HashMap<>();
        this.clubCounter    = 1;
        this.memberCounter  = 1;
        this.activityCounter = 1;
    }

    public String generateClubId() {
        return String.format("C%03d", clubCounter++);
    }


    public String generateMemberId() {
        return String.format("M%03d", memberCounter++);
    }


    public String generateActivityId() {
        return String.format("ACT%03d", activityCounter++);
    }


    public void addClub(Club club) throws InvalidClubDataException {
        if (clubMap.containsKey(club.getId())) {
            throw new InvalidClubDataException(
                    "A club with ID [" + club.getId() + "] already exists.", "DUPLICATE_CLUB");
        }
        clubs.add(club);
        clubMap.put(club.getId(), club);
    }


    public List<Club> getAllClubs() { return clubs; }


    public Club findClubById(String clubId) throws ClubNotFoundException {
        Club club = clubMap.get(clubId.toUpperCase());
        if (club == null) {
            throw new ClubNotFoundException(clubId);
        }
        return club;
    }

    public void updateClub(String clubId, String newName, String newDesc, int newMaxMembers)
            throws ClubNotFoundException {
        Club club = findClubById(clubId);
        if (newName != null && !newName.isBlank())    club.setClubName(newName);
        if (newDesc != null && !newDesc.isBlank())    club.setDescription(newDesc);
        if (newMaxMembers > 0)                        club.setMaxMembers(newMaxMembers);
    }

    public void deleteClub(String clubId) throws ClubNotFoundException {
        Club club = findClubById(clubId);

        for (StudentMember member : members) {
            member.removeClubId(clubId);
        }

        clubs.remove(club);
        clubMap.remove(clubId.toUpperCase());
    }

    public List<Club> searchClubsByName(String keyword) {
        List<Club> results = new ArrayList<>();
        for (Club c : clubs) {
            if (c.getClubName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(c);
            }
        }
        return results;
    }


    public List<Club> filterClubsByType(String type) {
        List<Club> results = new ArrayList<>();
        for (Club c : clubs) {
            if (c.getClubType().equalsIgnoreCase(type)) {
                results.add(c);
            }
        }
        return results;
    }

    public void registerMember(StudentMember member) throws InvalidClubDataException {
        if (memberMap.containsKey(member.getId())) {
            throw new InvalidClubDataException(
                    "A member with ID [" + member.getId() + "] already exists.", "DUPLICATE_MEMBER_ID");
        }
        members.add(member);
        memberMap.put(member.getId(), member);
    }


    public List<StudentMember> getAllMembers() { return members; }

    public StudentMember findMemberById(String memberId) throws InvalidClubDataException {
        StudentMember member = memberMap.get(memberId.toUpperCase());
        if (member == null) {
            throw new InvalidClubDataException(
                    "Member not found with ID: " + memberId, "MEMBER_NOT_FOUND");
        }
        return member;
    }

    public void enrollMemberInClub(String memberId, String clubId)
            throws ClubNotFoundException, InvalidClubDataException, DuplicateMemberException {
        Club club             = findClubById(clubId);
        StudentMember member  = findMemberById(memberId);

        if (club.hasMember(memberId)) {
            throw new DuplicateMemberException(memberId, clubId);
        }
        if (club.isFull()) {
            throw new InvalidClubDataException(
                    "Club [" + clubId + "] is full (" + club.getMaxMembers() + " members max).",
                    "CLUB_FULL");
        }

        club.addMember(memberId);
        member.addClubId(clubId);
    }

    public void removeMemberFromClub(String memberId, String clubId)
            throws ClubNotFoundException, InvalidClubDataException {
        Club club = findClubById(clubId);
        StudentMember member = findMemberById(memberId);
        String storedMemberId = member.getId();
        String storedClubId = club.getId();
        if (!club.hasMember(storedMemberId)) {
            throw new InvalidClubDataException(
                    "Member [" + memberId + "] is NOT a member of Club [" + clubId + "].",
                    "MEMBER_NOT_IN_CLUB");
        }
        club.removeMember(storedMemberId);
        member.removeClubId(storedClubId);
        if (storedMemberId.equalsIgnoreCase(club.getLeaderId())) {
            club.setLeaderId(null);
        }
        demoteLeaderOfClub(member, storedClubId);
    }

    private void demoteLeaderOfClub(StudentMember member, String clubId) {
        if (member instanceof ClubLeader leader
                && clubId.equalsIgnoreCase(leader.getLeadingClubId())) {
            StudentMember regular = new StudentMember(
                    member.getId(), member.getName(), member.getEmail(),
                    member.getAge(), member.getMajor());
            for (String otherClubId : member.getClubIds()) regular.addClubId(otherClubId);
            members.set(members.indexOf(member), regular);
            memberMap.put(regular.getId(), regular);
        }
    }

    public void assignClubLeader(String memberId, String clubId, String leadershipDate)
            throws ClubNotFoundException, InvalidClubDataException {
        Club club            = findClubById(clubId);
        StudentMember member = findMemberById(memberId);


        if (!club.hasMember(memberId)) {
            throw new InvalidClubDataException(
                    "Member [" + memberId + "] must be enrolled in Club [" + clubId + "] first.",
                    "NOT_YET_MEMBER");
        }


        ClubLeader leader = new ClubLeader(
                member.getId(), member.getName(), member.getEmail(),
                member.getAge(), member.getMajor(), clubId, leadershipDate);


        for (String cId : member.getClubIds()) {
            if (!cId.equals(clubId)) leader.addClubId(cId);
        }


        members.remove(member);
        members.add(leader);
        memberMap.put(leader.getId(), leader);

        club.setLeaderId(memberId);
    }


    public List<StudentMember> searchMembersByName(String keyword) {
        List<StudentMember> results = new ArrayList<>();
        for (StudentMember m : members) {
            if (m.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(m);
            }
        }
        return results;
    }

    public void addActivityToClub(String clubId, Activity activity)
            throws ClubNotFoundException {
        Club club = findClubById(clubId);
        club.addActivity(activity);
        allActivities.add(activity);
    }

    public List<Activity> getAllActivities() { return allActivities; }

    public int getTotalClubs()      { return clubs.size();    }

    public int getTotalMembers()    { return members.size();  }

    public int getTotalActivities() { return allActivities.size(); }

    public long countAcademicClubs() {
        return clubs.stream().filter(c -> c instanceof AcademicClub).count();
    }

    public long countSportsClubs() {
        return clubs.stream().filter(c -> c instanceof SportsClub).count();
    }

    public long countLeaders() {
        return members.stream().filter(m -> m instanceof ClubLeader).count();
    }
}