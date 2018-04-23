import { BaseEntity } from './../../shared';
import {User} from "../../shared";

export const enum UserBookingStatus {
    'Requested',
    'Apporved'
}

export class Booking implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public status?: UserBookingStatus,
        public bookingTime?: string,
        public bookingHour?: string,
        public court?: BaseEntity,
        public bookingUser?: User,
    ) {
    }
}
