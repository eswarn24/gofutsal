import { BaseEntity } from './../../shared';

export class BookingStatus implements BaseEntity {
    constructor(
        public id?: number,
        public status?: string,
    ) {
    }
}
