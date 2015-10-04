/**
 * AppointmentAbstractTrackedPane.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.internal.scene.control.skin.agenda.base24hour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jfxtras.scene.control.agenda.Agenda;

abstract class AppointmentAbstractTrackedPane extends AppointmentAbstractPane {
	/**
	 * @param calendar
	 * @param appointment
	 */
	AppointmentAbstractTrackedPane(LocalDate localDate, Agenda.Appointment appointment, LayoutHelp layoutHelp)
	{
		super(appointment, layoutHelp);
		
		// we know start and end optionally are set
		startDateTime = appointment.getStartLocalDateTime().toLocalDate().isEqual(localDate) ? appointment.getStartLocalDateTime() : localDate.atStartOfDay();
		if (appointment.getEndLocalDateTime() == null) {
			endDateTime = null; // a task does not have an end time 
			durationInMS = 0;
		}
		else {
			endDateTime = appointment.getEndLocalDateTime().toLocalDate().isEqual(localDate) ? appointment.getEndLocalDateTime() : localDate.plusDays(1).atStartOfDay();
			durationInMS = startDateTime.until(endDateTime, ChronoUnit.MILLIS);
		}
		firstPaneOfAppointment = startDateTime.equals(appointment.getStartLocalDateTime());
		lastPaneOfAppointment = (endDateTime != null && endDateTime.equals(appointment.getEndLocalDateTime()));
		intermediatePaneOfAppointment = !firstPaneOfAppointment && !lastPaneOfAppointment;
	}
	protected final LocalDateTime startDateTime;
	protected final LocalDateTime endDateTime;
	protected final long durationInMS;
	protected final boolean firstPaneOfAppointment;
	protected final boolean intermediatePaneOfAppointment;
	protected final boolean lastPaneOfAppointment;

	@Override
	protected  boolean showStartTimeText() {
		return firstPaneOfAppointment;
	}
	@Override
	protected  boolean showEndTimeText() {
		return lastPaneOfAppointment;
	}

	// for the role of cluster owner
	List<AppointmentAbstractTrackedPane> clusterMembers = new ArrayList<>(); 
	List<List<AppointmentAbstractTrackedPane>> clusterTracks = new ArrayList<>();
	
	// for the role of cluster member
	AppointmentAbstractTrackedPane clusterOwner = this;
	int clusterTrackIdx = -1;

	/**
	 * This method prepares a day for being drawn.
	 * The appointments within one day might overlap, this method will create a data structure so it is clear how these overlapping appointments should be drawn.
	 * All appointments in one day are process based on their start time; earliest first, and if there are more with the same start time, longest duration first.
	 * The appointments are then place onto (parallel) tracks; an appointment initially is placed in track 0. 
	 * But if there is already an (partially overlapping) appointment there, then the appointment is moved to track 1. 
	 * Unless there also is an appointment already in that track 1, then the next track is tried, and so forth, until a free track is found.
	 * For example (the letters are not the sequence in which the appointments are processed, they're just for identifying them):
	 * 
	 *  tracks
	 *  0 1 2 3
	 *  -------
	 *  . . . .
	 *  . . . .
	 *  A . . .
	 *  A B C .
	 *  A B C D
	 *  A B . D
	 *  A . . D
	 *  A E . D
	 *  A . . D
	 *  . . . D
	 *  . . . D
	 *  F . . D
	 *  F H . D 
	 *  . . . .
	 *  G . . . 
	 *  . . . .
	 * 
	 * Appointment A was rendered first and put into track 0 and its start time.
	 * Then appointment B was added, initially it was put in track 0, but appointment A already uses the that slot, so B was moved into track 1.
	 * C moved from track 0, conflicting with A, to track 1, conflicting with B, and ended up in track 2. And so forth.
	 * F and H show that even though D overlaps them, they could perfectly be placed in lower tracks.
	 * 
	 * A cluster of appointments always starts with a free standing appointment in track 0, for example A or G, such appointment is called the cluster owner.
	 * When the next appointment is added to the tracks, and finds that it cannot be put in track 0, it will be added as a member to the cluster represented by the appointment in track 0.
	 * Special attention must be paid to an appointment that is placed in track 0, but is linked to a cluster by a earlier appointment in a higher track; such an appointment is not the cluster owner.
	 * In the example above, F is linked through D to the cluster owned by A. So F is not a cluster owner, but a member of the cluster owned by A.
	 * And appointment H through F is also part of the cluster owned by A.  
	 * G finally starts a new cluster.
	 * The cluster owner knows all members and how many tracks there are, each member knows in what track it is and has a direct link to the cluster owner. 
	 *  
	 * When rendering the appointments above, parallel appointments are rendered narrower & indented, so appointments partially overlap and the left side of an appointment is always visible to the user.
	 * In the example above the single appointment G is rendered full width, while for example A, B, C and D are overlapping.
	 * F and H are drawn in the same dimensions as A and B in order to allow D to overlap then.
	 * The size and amount of indentation depends on the number of appointments that are rendered next to each other.
	 * In order to compute its location and size, each appointment needs to know:
	 * - its start and ending time,
	 * - its track number,
	 * - its total number of tracks,
	 * - and naturally the total width and height available to draw the day.
	 * 
	 */
	static List<? extends AppointmentAbstractTrackedPane> determineTracks(List<? extends AppointmentAbstractTrackedPane> appointmentAbstractTrackedPanes) {
		
		// sort on start time and then decreasing duration
		Collections.sort(appointmentAbstractTrackedPanes, new Comparator<AppointmentAbstractTrackedPane>() {
			@Override
			public int compare(AppointmentAbstractTrackedPane o1, AppointmentAbstractTrackedPane o2) {
				// if not same start, then compare on starttime
				if (!o1.startDateTime.isEqual(o2.startDateTime)) {
					return o1.startDateTime.compareTo(o2.startDateTime);
				}
				
				// task after appointment
				if (o1 instanceof AppointmentRegularBodyPane && o2 instanceof AppointmentTaskBodyPane) {
					return -1;
				}
				if (o1 instanceof AppointmentTaskBodyPane && o2 instanceof AppointmentRegularBodyPane) {
					return 1;
				}
				
				// longest last 
				return o1.durationInMS == o2.durationInMS ? 0 : (o1.durationInMS > o2.durationInMS ? -1 : 1);
			}
		});
		
		// start placing appointments in the tracks
		AppointmentAbstractTrackedPane lClusterOwner = null;
		for (AppointmentAbstractTrackedPane lAppointmentPane : appointmentAbstractTrackedPanes) 
		{
			// if there is no cluster owner
			if (lClusterOwner == null) {
				
				// than the current becomes an owner
				// only create a minimal cluster, because it will be setup fully in the code below
				lClusterOwner = lAppointmentPane;
				lClusterOwner.clusterTracks = new ArrayList<List<AppointmentAbstractTrackedPane>>();
			}
			
			// in which track should it be added
			int lTrackNr = determineTrackWhereAppointmentCanBeAdded(lClusterOwner.clusterTracks, lAppointmentPane);
			// if it can be added to track 0, then we have a "situation". Track 0 could mean
			// - we must start a new cluster
			// - the appointment is still linked to the running cluster by means of a linking appointment in the higher tracks
			if (lTrackNr == 0) {
				
				// So let's see if there is a linking appointment higher up
				boolean lOverlaps = false;
				for (int i = 1; i < lClusterOwner.clusterTracks.size() && lOverlaps == false; i++) {
					lOverlaps = checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(lClusterOwner.clusterTracks, i, lAppointmentPane);
				}
				
				// if it does not overlap, we start a new cluster
				if (lOverlaps == false) {
					lClusterOwner = lAppointmentPane;
					lClusterOwner.clusterMembers = new ArrayList<AppointmentAbstractTrackedPane>(); 
					lClusterOwner.clusterTracks = new ArrayList<List<AppointmentAbstractTrackedPane>>();
					lClusterOwner.clusterTracks.add(new ArrayList<AppointmentAbstractTrackedPane>());
				}
			}
			
			// add it to the track (and setup all other cluster data)
			lClusterOwner.clusterMembers.add(lAppointmentPane);
			lClusterOwner.clusterTracks.get(lTrackNr).add(lAppointmentPane);
			lAppointmentPane.clusterOwner = lClusterOwner;
			lAppointmentPane.clusterTrackIdx = lTrackNr;				
			// for debug  System.out.println("----"); for (int i = 0; i < lClusterOwner.clusterTracks.size(); i++) { System.out.println(i + ": " + lClusterOwner.clusterTracks.get(i) ); } System.out.println("----");
		}
		
		// done
		return appointmentAbstractTrackedPanes;
	}
	
	/**
	 * 
	 */
	static private int determineTrackWhereAppointmentCanBeAdded(List<List<AppointmentAbstractTrackedPane>> tracks, AppointmentAbstractTrackedPane appointmentPane)
	{
		int lTrackNr = 0;
		while (true)
		{
			// make sure there is a arraylist for this track
			if (lTrackNr == tracks.size()) {
				tracks.add(new ArrayList<AppointmentAbstractTrackedPane>());
			}
			
			// scan all existing appointments in this track and see if there is an overlap
			if (checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(tracks, lTrackNr, appointmentPane) == false)
			{
				// no overlap, it can be added here
				return lTrackNr;
			}

			// overlap, try next track
			lTrackNr++;
		}
	}
	
	/**
	 * 
	 */
	static private boolean checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(List<List<AppointmentAbstractTrackedPane>> tracks, int tracknr, AppointmentAbstractTrackedPane newAppointmentPane)
	{
		// get the track
		List<AppointmentAbstractTrackedPane> lTrack = tracks.get(tracknr);
		
		// scan all existing appointments in this track
		for (AppointmentAbstractTrackedPane lAppointmentPane : lTrack)
		{
			// There is an overlap:
			// if the start time of the already placed appointment is before or equals the new appointment's end time 
			// and the end time of the already placed appointment is after the new appointment's start time (equals will put two consequative appointments into separate tracks) 
			// ...PPPPPPPPP...    
			// .NNNN.......... -> Ps <= Ne & Pe >= Ns -> overlap
			// .....NNNNN..... -> Ps <= Ne & Pe >= Ns -> overlap
			// ..........NNN.. -> Ps <= Ne & Pe >= Ns -> overlap
			// .NNNNNNNNNNNNN. -> Ps <= Ne & Pe >= Ns -> overlap
			// .N............. -> false    & Pe >= Ns -> no overlap
			// .............N. -> Ps <= Ne & false	  -> no overlap
			LocalDateTime lPlacedStart = lAppointmentPane.startDateTime;
			LocalDateTime lPlacedEnd = (lAppointmentPane.endDateTime != null ? lAppointmentPane.endDateTime : lAppointmentPane.startDateTime.plusMinutes(10));
			LocalDateTime lNewStart = newAppointmentPane.startDateTime;
			LocalDateTime lNewEnd = (newAppointmentPane.endDateTime != null ? newAppointmentPane.endDateTime : newAppointmentPane.startDateTime.plusMinutes(10));
			if ( (lPlacedStart.isEqual(lNewStart) || lNewEnd == null || lPlacedStart.isBefore(lNewEnd)) 
			  && lPlacedEnd != null && lPlacedEnd.isAfter(lNewStart)
			   )
			{
				// overlap
				return true;
			}
		}
		
		// no overlap
		return false;
	}
	
	/**
	 * 
	 */
//	static private boolean checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(List<List<AppointmentAbstractTrackedPane>> tracks, int tracknr, AppointmentAbstractTrackedPane newAppointmentPane)
//	{
//		// get the track
//		List<AppointmentAbstractTrackedPane> lTrack = tracks.get(tracknr);
//		if (lTrack.size() == 0) {
//			return false; // no overlap
//		}
//		
//		// scan all existing appointments in this track
//		for (AppointmentAbstractTrackedPane lPlacedAppointmentPane : lTrack)
//		{
//			// There is an overlap:
//			// if the start time of the already placed appointment is before or equals the new appointment's end time 
//			// and the end time of the already placed appointment is after or equals the new appointment's start time
//			//
//			// ...PPPPPPPPP... already placed appointment with end date
//			//
//			// .NNNN.......... -> overlap
//			// .....NNNNN..... -> overlap
//			// ..........NNN.. -> overlap
//			// .NNNNNNNNNNNNN. -> overlap
//			// .N............. -> no overlap -> N-end <= P-start [1]
//			// .............N. -> no overlap -> N-start >= p-end [2]
//			// .NNNNNNNNNNNNNN -> overlap (new without end date)
//			// ......NNNNNNNNN -> overlap (new without end date)
//			// .............NN -> no overlap (new without end date) -> N-start >= p-end [3]
//			//
//			// ...PPPPPPPPPPPP already placed appointment without end date
//			// .NNNN.......... -> overlap
//			// .....NNNNN..... -> overlap
//			// ..........NNN.. -> overlap
//			// .NNNNNNNNNNNNN. -> overlap
//			// .N............. -> no overlap -> N-end <= P-start [4]
//			// .............N. -> overlap
//			// .NNNNNNNNNNNNNN -> overlap (new without end date)
//			// ......NNNNNNNNN -> overlap (new without end date)
//			// .............NN -> overlap (new without end date)
//			if ( (lPlacedAppointmentPane.endDateTime != null && newAppointmentPane.endDateTime != null && isBeforeOrEqual(newAppointmentPane.endDateTime, lPlacedAppointmentPane.startDateTime)) // [1]
//			  || (lPlacedAppointmentPane.endDateTime != null && newAppointmentPane.endDateTime != null && isAfterOrEqual(newAppointmentPane.startDateTime, lPlacedAppointmentPane.endDateTime)) // [2]
//			  || (lPlacedAppointmentPane.endDateTime != null && newAppointmentPane.endDateTime == null && isAfterOrEqual(newAppointmentPane.startDateTime, lPlacedAppointmentPane.endDateTime)) // [3]
//			  || (lPlacedAppointmentPane.endDateTime == null && newAppointmentPane.endDateTime != null && isBeforeOrEqual(newAppointmentPane.endDateTime, lPlacedAppointmentPane.startDateTime)) // [4]
//			   )
//			{
//				// no overlap
//				return false;
//			}
//		}
//		// overlap
//		return true;
//	}
//
//	static private boolean isBeforeOrEqual(LocalDateTime ldt1, LocalDateTime ldt2) {
//		return ldt1.isBefore(ldt2) || ldt1.isEqual(ldt2);
//	}
//	
//	static private boolean isAfterOrEqual(LocalDateTime ldt1, LocalDateTime ldt2) {
//		return ldt1.isAfter(ldt2) || ldt1.isEqual(ldt2);
//	}

	/**
	 * 
	 */
	public String toString()
	{
		return "pane=" + startDateTime + "-" + endDateTime
		     + ";" 
		     + super.toString()
			 ;
	}
}
