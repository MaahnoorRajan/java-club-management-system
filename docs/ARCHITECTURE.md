# Architecture and tradeoffs

## Object model

- Abstract User implements Manageable; StudentMember extends User; ClubLeader extends StudentMember.
- Abstract Club implements Manageable; AcademicClub and SportsClub provide specialised fields and display methods.
- Activity represents a club event. Club contains member IDs and activity objects.
- ReportGenerator implements Reportable and uses ClubManager to produce console reports.
- Custom checked exceptions distinguish missing clubs, duplicate enrollment, and other invalid operations.

The console menu delegates application operations to ClubManager. That manager keeps lists for iteration and maps for ID lookup. This is a useful coursework design, although maintaining both structures requires disciplined updates.

## Leadership removal

The manager resolves input IDs to stored objects, removes membership on both sides, clears a matching club leader reference, and converts a leader of that club into a regular StudentMember. Remaining memberships are copied; the member list and map both reference the replacement object. Removing membership of another club does not demote a leader.

## Club deletion

Deletion resolves the stored club ID, removes it from all members, demotes a member whose recorded leadership points to that club, removes matching activities from the global collection, and removes the club from both list and map. Other clubs and activities remain intact.

## Known design boundaries

A role would usually be better represented as a relationship between a member and a club than by replacing the member object. The current single-leading-club subtype is retained to keep these maintenance changes focused. Reassignment and a person leading several clubs remain limitations.

Unmodifiable collection views, validated model methods, LocalDate values, and persistent storage are possible follow-up work. They have not been added or claimed in this version.
