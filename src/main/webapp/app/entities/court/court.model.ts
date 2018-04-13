import { BaseEntity, User } from './../../shared';

export const enum Region {
    'Petaling Jaya',
    'Kelana Jaya',
    'Putra Jaya',
    'Kuala lumpur',
    'Subang Jaya',
    'Damansara',
    'USJ',
    'Puchong',
    'Nilai',
    'Bukit Bintang'
}

export class Court implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public rate?: string,
        public court?: Region,
        public courtLocation?: BaseEntity,
        public courtType?: BaseEntity,
        public user?: User,
    ) {
    }
}
