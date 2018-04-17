import { BaseEntity } from './../../shared';

export const enum State {
    'Johor',
    'Kedah',
    'Kelantan',
    'Melaka',
    'Negeri Sembilan',
    'Pahang',
    'Penang',
    'Perak',
    'Perlis',
    'Sabah',
    'Sarawak',
    'Selangor',
    'Terengganu',
    'Kuala Lumpur',
    'Labuan',
    'Putrajaya'
}

export class CourtLocation implements BaseEntity {
    constructor(
        public id?: number,
        public address?: string,
        public country?: string,
        public state?: State,
        public center?: string,
    ) {
    }
}
